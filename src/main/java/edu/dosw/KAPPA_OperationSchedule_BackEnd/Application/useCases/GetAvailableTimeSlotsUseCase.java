package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetAvailableTimeSlotsUseCase {

    private final TimeSlotRepositoryPort timeSlotRepository;
    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;
    private final CategoryScheduleRepositoryPort categoryScheduleRepository;
    private final GenerateTimeSlotsUseCase generateTimeSlotsUseCase;

    public GetAvailableTimeSlotsUseCase(
            TimeSlotRepositoryPort timeSlotRepository,
            OperatingHoursRepositoryPort operatingHoursRepository,
            TemporaryClosureRepositoryPort temporaryClosureRepository,
            CategoryScheduleRepositoryPort categoryScheduleRepository,
            GenerateTimeSlotsUseCase generateTimeSlotsUseCase) {
        this.timeSlotRepository = timeSlotRepository;
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
        this.categoryScheduleRepository = categoryScheduleRepository;
        this.generateTimeSlotsUseCase = generateTimeSlotsUseCase;
    }

    public List<TimeSlot> getAvailableTimeSlots(String pointOfSaleId, LocalDate date) {
        return getAvailableTimeSlots(pointOfSaleId, date, null);
    }

    public List<TimeSlot> getAvailableTimeSlots(String pointOfSaleId, LocalDate date, String productCategory) {
        if (date.isBefore(LocalDate.now())) {
            throw BusinessException.validationError("No se pueden buscar horarios disponibles en fechas pasadas");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<TimeSlot> existingSlots = timeSlotRepository
                .findByPointOfSaleIdAndDateTimeRange(pointOfSaleId, startOfDay, endOfDay);

        if (existingSlots.isEmpty()) {
            existingSlots = generateTimeSlotsUseCase.generateForDate(pointOfSaleId, date);
        }

        return existingSlots.stream()
                .filter(TimeSlot::isAvailable)
                .filter(TimeSlot::getAvailable)
                .filter(slot -> isNotDuringClosure(pointOfSaleId, slot))
                .filter(slot -> isWithinCategoryHours(slot, productCategory))
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableTimeSlotsWithClosuresValidation(String pointOfSaleId, LocalDate date) {
        List<TimeSlot> allAvailableSlots = getAvailableTimeSlots(pointOfSaleId, date);

        return allAvailableSlots.stream()
                .filter(slot -> {
                    List<edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure> closures =
                            temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, slot.getStartTime());
                    return closures.isEmpty();
                })
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableTimeSlotsForNow(String pointOfSaleId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursLater = now.plusHours(2);

        List<TimeSlot> upcomingSlots = timeSlotRepository
                .findByPointOfSaleIdAndDateTimeRange(pointOfSaleId, now, twoHoursLater);

        return upcomingSlots.stream()
                .filter(TimeSlot::isAvailable)
                .filter(TimeSlot::getAvailable)
                .filter(slot -> slot.getStartTime().isAfter(now))
                .filter(slot -> isNotDuringClosure(pointOfSaleId, slot))
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableTimeSlotsWithMinCapacity(
            String pointOfSaleId, LocalDate date, Integer requiredCapacity) {

        List<TimeSlot> availableSlots = getAvailableTimeSlots(pointOfSaleId, date);

        return availableSlots.stream()
                .filter(slot -> slot.getAvailableCapacity() >= requiredCapacity)
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableTimeSlotsByProductCategory(
            String pointOfSaleId, LocalDate date, String productCategory) {

        if (productCategory == null || productCategory.isEmpty()) {
            return getAvailableTimeSlots(pointOfSaleId, date);
        }

        Optional<edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule> category =
                categoryScheduleRepository.findActiveByCategoryName(productCategory);

        if (category.isEmpty()) {
            throw BusinessException.validationError(
                    "La categoría de producto '" + productCategory + "' no existe o no está activa"
            );
        }

        return getAvailableTimeSlots(pointOfSaleId, date, productCategory);
    }


    private boolean isNotDuringClosure(String pointOfSaleId, TimeSlot slot) {
        List<edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure> closures =
                temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(
                        pointOfSaleId, slot.getStartTime());
        return closures.isEmpty();
    }

    private boolean isWithinCategoryHours(TimeSlot slot, String productCategory) {
        if (productCategory == null || productCategory.isEmpty()) {
            return true;
        }

        Optional<edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule> category =
                categoryScheduleRepository.findActiveByCategoryName(productCategory);

        if (category.isEmpty()) {
            return true;
        }

        edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule schedule = category.get();
        LocalTime slotStartTime = slot.getStartTime().toLocalTime();

        return !slotStartTime.isBefore(schedule.getStartTime()) &&
                !slotStartTime.isAfter(schedule.getEndTime());
    }
}