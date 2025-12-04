package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.GenerateTimeSlotsCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateTimeSlotsUseCase {

    private final TimeSlotRepositoryPort timeSlotRepository;
    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;

    public GenerateTimeSlotsUseCase(TimeSlotRepositoryPort timeSlotRepository,
                                    OperatingHoursRepositoryPort operatingHoursRepository,
                                    TemporaryClosureRepositoryPort temporaryClosureRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
    }

    public List<TimeSlot> execute(GenerateTimeSlotsCommand command) {
        if (command.getDate().isBefore(java.time.LocalDate.now())) {
            throw BusinessException.validationError("No se pueden generar slots para fechas pasadas");
        }

        if (command.getSlotDurationMinutes() <= 0) {
            throw BusinessException.validationError("La duración del slot debe ser mayor a 0 minutos");
        }

        if (command.getDefaultCapacity() <= 0) {
            throw BusinessException.validationError("La capacidad por defecto debe ser mayor a 0");
        }

        DayOfWeek dayOfWeek = command.getDate().getDayOfWeek();
        List<OperatingHours> operatingHours = operatingHoursRepository
                .findByPointOfSaleIdAndDayOfWeek(command.getPointOfSaleId(), dayOfWeek)
                .stream()
                .filter(OperatingHours::getActive)
                .toList();

        if (operatingHours.isEmpty()) {
            throw BusinessException.validationError(
                    "No hay horarios operativos configurados para el punto " +
                            command.getPointOfSaleId() + " en " + dayOfWeek
            );
        }

        List<TimeSlot> generatedSlots = new ArrayList<>();

        for (OperatingHours hours : operatingHours) {
            LocalTime currentTime = hours.getOpeningTime();
            LocalTime closingTime = hours.getClosingTime();

            while (currentTime.isBefore(closingTime)) {
                LocalTime endTime = currentTime.plusMinutes(command.getSlotDurationMinutes());

                if (endTime.isAfter(closingTime)) {
                    endTime = closingTime;
                }

                LocalDateTime slotStart = LocalDateTime.of(command.getDate(), currentTime);
                LocalDateTime slotEnd = LocalDateTime.of(command.getDate(), endTime);

                List<edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure> closures =
                        temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(
                                command.getPointOfSaleId(), slotStart);

                if (!closures.isEmpty()) {
                    currentTime = endTime;
                    continue;
                }

                boolean slotExists = timeSlotRepository
                        .findByPointOfSaleIdAndSlot(command.getPointOfSaleId(), slotStart, slotEnd)
                        .isPresent();

                if (!slotExists) {
                    TimeSlot slot = TimeSlot.builder()
                            .pointOfSaleId(command.getPointOfSaleId())
                            .startTime(slotStart)
                            .endTime(slotEnd)
                            .availableCapacity(command.getDefaultCapacity())
                            .bookedCount(0)
                            .available(true)
                            .build();

                    timeSlotRepository.save(slot);
                    generatedSlots.add(slot);
                }

                currentTime = endTime;

                if (currentTime.equals(closingTime)) {
                    break;
                }
            }
        }

        return generatedSlots;
    }

    public List<TimeSlot> generateForToday(String pointOfSaleId) {
        GenerateTimeSlotsCommand command = new GenerateTimeSlotsCommand();
        command.setPointOfSaleId(pointOfSaleId);
        command.setDate(java.time.LocalDate.now());
        command.setSlotDurationMinutes(30);  // Valor por defecto
        command.setDefaultCapacity(10);      // Valor por defecto
        return execute(command);
    }

    public List<TimeSlot> generateForTomorrow(String pointOfSaleId) {
        GenerateTimeSlotsCommand command = new GenerateTimeSlotsCommand();
        command.setPointOfSaleId(pointOfSaleId);
        command.setDate(java.time.LocalDate.now().plusDays(1));
        command.setSlotDurationMinutes(30);  // Valor por defecto
        command.setDefaultCapacity(10);      // Valor por defecto
        return execute(command);
    }

    public List<TimeSlot> generateForDateRange(String pointOfSaleId,
                                               java.time.LocalDate startDate,
                                               java.time.LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha fin");
        }

        List<TimeSlot> allSlots = new ArrayList<>();
        java.time.LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            GenerateTimeSlotsCommand command = new GenerateTimeSlotsCommand();
            command.setPointOfSaleId(pointOfSaleId);
            command.setDate(currentDate);
            command.setSlotDurationMinutes(30);
            command.setDefaultCapacity(10);

            allSlots.addAll(execute(command));
            currentDate = currentDate.plusDays(1);
        }

        return allSlots;
    }

    public List<TimeSlot> generateForDate(String pointOfSaleId, LocalDate date) {
        GenerateTimeSlotsCommand command = new GenerateTimeSlotsCommand();
        command.setPointOfSaleId(pointOfSaleId);
        command.setDate(date);
        command.setSlotDurationMinutes(30);
        command.setDefaultCapacity(10);
        return execute(command);
    }
}