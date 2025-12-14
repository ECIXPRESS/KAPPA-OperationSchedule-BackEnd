package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ValidateAvailabilityUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;
    private final CategoryScheduleRepositoryPort categoryScheduleRepository;
    private final TimeSlotRepositoryPort timeSlotRepository;

    public ValidateAvailabilityUseCase(
            OperatingHoursRepositoryPort operatingHoursRepository,
            TemporaryClosureRepositoryPort temporaryClosureRepository,
            CategoryScheduleRepositoryPort categoryScheduleRepository,
            TimeSlotRepositoryPort timeSlotRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
        this.categoryScheduleRepository = categoryScheduleRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public AvailabilityResult validatePointOfSaleAvailability(String pointOfSaleId, LocalDateTime requestedTime) {
        return validateAvailability(pointOfSaleId, requestedTime, null);
    }

    public AvailabilityResult validateProductCategoryAvailability(String pointOfSaleId, LocalDateTime requestedTime, String productCategory) {
        return validateAvailability(pointOfSaleId, requestedTime, productCategory);
    }

    /**
     * Método principal unificado que valida disponibilidad considerando:
     * 1. Cierres temporales
     * 2. Horarios operativos
     * 3. Categorías horarias (si aplica)
     * 4. Capacidad disponible en slots
     */
    public AvailabilityResult validateAvailability(String pointOfSaleId, LocalDateTime requestedTime, String productCategory) {
        if (requestedTime.isBefore(LocalDateTime.now())) {
            throw BusinessException.validationError("No se puede validar disponibilidad en fechas/horas pasadas");
        }

        List<TemporaryClosure> activeClosures = temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime);
        if (!activeClosures.isEmpty()) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "Punto de venta cerrado temporalmente");
        }

        DayOfWeek dayOfWeek = requestedTime.getDayOfWeek();
        LocalTime requestedLocalTime = requestedTime.toLocalTime();

        List<OperatingHours> operatingHours = operatingHoursRepository
                .findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek)
                .stream()
                .filter(OperatingHours::getActive)
                .toList();

        if (operatingHours.isEmpty()) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "Punto de venta no tiene horarios configurados para este día");
        }

        boolean isWithinOperatingHours = operatingHours.stream()
                .anyMatch(oh -> !requestedLocalTime.isBefore(oh.getOpeningTime()) &&
                        !requestedLocalTime.isAfter(oh.getClosingTime()));

        if (!isWithinOperatingHours) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "Fuera del horario de atención");
        }

        if (productCategory != null && !productCategory.isEmpty()) {
            Optional<CategorySchedule> categorySchedule = categoryScheduleRepository.findActiveByCategoryName(productCategory);

            if (categorySchedule.isEmpty()) {
                return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                        "La categoría de producto '" + productCategory + "' no existe o no está activa");
            }

            CategorySchedule schedule = categorySchedule.get();
            if (requestedLocalTime.isBefore(schedule.getStartTime()) || requestedLocalTime.isAfter(schedule.getEndTime())) {
                AvailabilityResult result = new AvailabilityResult(false, pointOfSaleId, requestedTime, "Producto fuera de horario");
                result.setCategoryMessage("Disponible solo en horario de " + productCategory + ": " +
                        schedule.getStartTime() + " - " + schedule.getEndTime());
                return result;
            }
        }

        boolean hasAvailableSlot = hasAvailableSlotWithCapacity(pointOfSaleId, requestedTime);
        if (!hasAvailableSlot) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "No hay slots disponibles con capacidad en ese horario");
        }

        AvailabilityResult result = new AvailabilityResult(true, pointOfSaleId, requestedTime, "Disponible");
        if (productCategory != null && !productCategory.isEmpty()) {
            result.setCategoryMessage("Producto dentro de horario permitido");
        }
        return result;
    }

    /**
     * Valida si hay al menos un slot disponible con capacidad en el horario solicitado
     */
    private boolean hasAvailableSlotWithCapacity(String pointOfSaleId, LocalDateTime requestedTime) {
        LocalDateTime slotStart = requestedTime;
        LocalDateTime slotEnd = requestedTime.plusMinutes(30);

        List<TimeSlot> slots = timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(
                pointOfSaleId, slotStart.minusMinutes(15), slotEnd.plusMinutes(15));


        return slots.stream()
                .anyMatch(slot ->
                        !requestedTime.isBefore(slot.getStartTime()) &&
                                !requestedTime.isAfter(slot.getEndTime()) &&
                                slot.getAvailable() &&
                                slot.isAvailable()
                );
    }

    /**
     * Método que sugiere horarios alternativos
     */
    public AvailabilityResult validateAvailabilityWithSuggestions(String pointOfSaleId, LocalDateTime requestedTime, String productCategory) {
        AvailabilityResult result = validateAvailability(pointOfSaleId, requestedTime, productCategory);

        if (!result.getAvailable()) {
            List<TimeSlot> nextAvailableSlots = findNextAvailableSlots(pointOfSaleId, requestedTime, 3);
            if (!nextAvailableSlots.isEmpty()) {
                result.setAvailableTimeSlots(nextAvailableSlots.stream()
                        .map(slot -> slot.getStartTime().toString())
                        .toList());
            }
        }

        return result;
    }

    /**
     * Encuentra los próximos N slots disponibles después del tiempo solicitado
     */
    private List<TimeSlot> findNextAvailableSlots(String pointOfSaleId, LocalDateTime fromTime, int limit) {
        LocalDateTime endOfDay = fromTime.toLocalDate().atTime(23, 59, 59);

        List<TimeSlot> futureSlots = timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(
                pointOfSaleId, fromTime, endOfDay);

        return futureSlots.stream()
                .filter(TimeSlot::getAvailable)
                .filter(TimeSlot::isAvailable)
                .limit(limit)
                .toList();
    }

    /**
     * Valida disponibilidad para un pedido completo (múltiples productos)
     */
    public AvailabilityResult validateOrderAvailability(String pointOfSaleId, LocalDateTime requestedTime, List<String> productCategories) {
        for (String category : productCategories) {
            AvailabilityResult categoryResult = validateAvailability(pointOfSaleId, requestedTime, category);
            if (!categoryResult.getAvailable()) {
                return categoryResult;
            }
        }

        return new AvailabilityResult(true, pointOfSaleId, requestedTime,
                "Todos los productos disponibles en el horario solicitado");
    }
}