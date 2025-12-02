package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetAvailableTimeSlotsUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;

    public GetAvailableTimeSlotsUseCase(OperatingHoursRepositoryPort operatingHoursRepository, TemporaryClosureRepositoryPort temporaryClosureRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
    }

    public List<TimeSlot> getAvailableTimeSlots(String pointOfSaleId, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw BusinessException.validationError("No se pueden buscar horarios disponibles en fechas pasadas");
        }

        List<TimeSlot> availableSlots = new ArrayList<>();
        List<OperatingHours> operatingHours = operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, date.getDayOfWeek());

        if (operatingHours.isEmpty()) {
            throw BusinessException.validationError("El punto de venta " + pointOfSaleId + " no tiene horarios configurados para " + date.getDayOfWeek());
        }

        for (OperatingHours hours : operatingHours) {
            if (hours.getActive()) {
                LocalTime currentTime = hours.getOpeningTime();
                LocalTime closingTime = hours.getClosingTime();

                while (currentTime.isBefore(closingTime)) {
                    LocalTime endTime = currentTime.plusMinutes(30);

                    if (endTime.isAfter(closingTime)) {
                        break;
                    }

                    LocalDateTime slotStart = LocalDateTime.of(date, currentTime);
                    List<TemporaryClosure> closures = temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, slotStart);

                    if (closures.isEmpty()) {
                        LocalDateTime slotEnd = LocalDateTime.of(date, endTime);
                        TimeSlot slot = new TimeSlot(slotStart, slotEnd, 5);
                        availableSlots.add(slot);
                    }

                    currentTime = endTime;
                }
            }
        }

        return availableSlots;
    }
}