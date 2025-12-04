package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleReportsUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;
    private final CategoryScheduleRepositoryPort categoryScheduleRepository;
    private final TimeSlotRepositoryPort timeSlotRepository;

    public ScheduleReportsUseCase(
            OperatingHoursRepositoryPort operatingHoursRepository,
            TemporaryClosureRepositoryPort temporaryClosureRepository,
            CategoryScheduleRepositoryPort categoryScheduleRepository,
            TimeSlotRepositoryPort timeSlotRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
        this.categoryScheduleRepository = categoryScheduleRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public Map<String, Object> generatePointOfSaleReport(String pointOfSaleId) {
        List<OperatingHours> operatingHours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        if (operatingHours.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }

        Map<String, Object> report = new HashMap<>();

        List<OperatingHours> allOperatingHours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        List<TemporaryClosure> closures = temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);
        List<TimeSlot> timeSlots = timeSlotRepository.findByPointOfSaleId(pointOfSaleId);

        // Calcular métricas de ocupación de slots
        Map<String, Object> slotMetrics = calculateSlotMetrics(timeSlots);

        report.put("pointOfSaleId", pointOfSaleId);
        report.put("operatingHours", allOperatingHours);
        report.put("temporaryClosures", closures);
        report.put("closureCount", closures.size());
        report.put("timeSlotMetrics", slotMetrics);
        report.put("totalTimeSlots", timeSlots.size());

        return report;
    }

    public Map<String, Object> generateAvailabilityReport(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha de fin");
        }

        Map<String, Object> report = new HashMap<>();

        List<OperatingHours> allOperatingHours = operatingHoursRepository.findAllActive();
        List<TemporaryClosure> closuresInRange = temporaryClosureRepository.findActiveClosuresInRange(start, end);

        // Obtener slots en el rango para todos los puntos de venta
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll();
        List<TimeSlot> slotsInRange = allTimeSlots.stream()
                .filter(slot ->
                        !slot.getStartTime().isBefore(start) &&
                                !slot.getEndTime().isAfter(end))
                .toList();

        Map<String, Object> slotOccupancyReport = generateSlotOccupancyReport(slotsInRange);

        report.put("reportPeriod", Map.of("start", start, "end", end));
        report.put("totalOperatingHours", allOperatingHours.size());
        report.put("closuresInPeriod", closuresInRange.size());
        report.put("closuresDetails", closuresInRange);
        report.put("slotOccupancy", slotOccupancyReport);
        report.put("totalSlotsInPeriod", slotsInRange.size());

        return report;
    }

    public Map<String, Object> generateCategoryScheduleReport() {
        Map<String, Object> report = new HashMap<>();

        List<CategorySchedule> allCategories = categoryScheduleRepository.findAll();
        List<CategorySchedule> activeCategories = categoryScheduleRepository.findAllActive();

        report.put("totalCategories", allCategories.size());
        report.put("activeCategories", activeCategories.size());
        report.put("categories", allCategories);

        return report;
    }

    // ========== NUEVOS MÉTODOS PARA TIME SLOTS ========== //

    /**
     * Genera reporte de ocupación de slots para un punto de venta específico
     */
    public Map<String, Object> generateTimeSlotOccupancyReport(String pointOfSaleId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha de fin");
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<TimeSlot> slots = timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(pointOfSaleId, start, end);

        if (slots.isEmpty()) {
            throw BusinessException.validationError(
                    "No hay slots registrados para el punto de venta " + pointOfSaleId +
                            " en el rango especificado"
            );
        }

        return generateSlotOccupancyReport(slots);
    }

    /**
     * Reporte de slots más ocupados (horas pico)
     */
    public Map<String, Object> generatePeakHoursReport(String pointOfSaleId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<TimeSlot> slots = timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(pointOfSaleId, start, end);

        // Agrupar por hora y calcular ocupación promedio
        Map<Integer, List<TimeSlot>> slotsByHour = slots.stream()
                .collect(Collectors.groupingBy(slot -> slot.getStartTime().getHour()));

        Map<String, Object> report = new HashMap<>();
        Map<String, Double> occupancyByHour = new TreeMap<>();

        for (Map.Entry<Integer, List<TimeSlot>> entry : slotsByHour.entrySet()) {
            int hour = entry.getKey();
            List<TimeSlot> hourSlots = entry.getValue();

            double avgOccupancy = hourSlots.stream()
                    .mapToDouble(slot ->
                            (double) slot.getBookedCount() / slot.getAvailableCapacity() * 100)
                    .average()
                    .orElse(0.0);

            occupancyByHour.put(String.format("%02d:00-%02d:00", hour, hour+1), avgOccupancy);
        }

        // Identificar horas pico (ocupación > 80%)
        List<String> peakHours = occupancyByHour.entrySet().stream()
                .filter(entry -> entry.getValue() > 80.0)
                .map(Map.Entry::getKey)
                .toList();

        report.put("date", date.toString());
        report.put("pointOfSaleId", pointOfSaleId);
        report.put("occupancyByHour", occupancyByHour);
        report.put("peakHours", peakHours);
        report.put("totalSlotsAnalyzed", slots.size());

        return report;
    }

    /**
     * Reporte de capacidad utilizada vs disponible
     */
    public Map<String, Object> generateCapacityUtilizationReport(String pointOfSaleId) {
        List<TimeSlot> allSlots = timeSlotRepository.findByPointOfSaleId(pointOfSaleId);

        if (allSlots.isEmpty()) {
            throw BusinessException.validationError(
                    "No hay slots registrados para el punto de venta " + pointOfSaleId
            );
        }

        int totalCapacity = allSlots.stream()
                .mapToInt(TimeSlot::getAvailableCapacity)
                .sum();

        int totalBooked = allSlots.stream()
                .mapToInt(TimeSlot::getBookedCount)
                .sum();

        double utilizationRate = totalCapacity > 0 ?
                (double) totalBooked / totalCapacity * 100 : 0.0;

        Map<String, Object> report = new HashMap<>();
        report.put("pointOfSaleId", pointOfSaleId);
        report.put("totalCapacity", totalCapacity);
        report.put("totalBooked", totalBooked);
        report.put("availableCapacity", totalCapacity - totalBooked);
        report.put("utilizationRate", String.format("%.2f%%", utilizationRate));
        report.put("totalSlots", allSlots.size());

        // Slots con alta ocupación (>90%)
        long highOccupancySlots = allSlots.stream()
                .filter(slot -> {
                    if (slot.getAvailableCapacity() == 0) return false;
                    double occupancy = (double) slot.getBookedCount() / slot.getAvailableCapacity() * 100;
                    return occupancy > 90.0;
                })
                .count();

        report.put("highOccupancySlots", highOccupancySlots);
        report.put("highOccupancyPercentage",
                String.format("%.2f%%", (double) highOccupancySlots / allSlots.size() * 100));

        return report;
    }

    // ========== MÉTODOS PRIVADOS DE AYUDA ========== //

    private Map<String, Object> calculateSlotMetrics(List<TimeSlot> slots) {
        Map<String, Object> metrics = new HashMap<>();

        if (slots.isEmpty()) {
            metrics.put("totalSlots", 0);
            metrics.put("availableSlots", 0);
            metrics.put("fullyBookedSlots", 0);
            metrics.put("averageOccupancy", "0%");
            return metrics;
        }

        int totalSlots = slots.size();
        long availableSlots = slots.stream()
                .filter(TimeSlot::isAvailable)
                .count();

        long fullyBookedSlots = slots.stream()
                .filter(slot -> slot.getBookedCount() >= slot.getAvailableCapacity())
                .count();

        double avgOccupancy = slots.stream()
                .filter(slot -> slot.getAvailableCapacity() > 0)
                .mapToDouble(slot -> (double) slot.getBookedCount() / slot.getAvailableCapacity())
                .average()
                .orElse(0.0) * 100;

        metrics.put("totalSlots", totalSlots);
        metrics.put("availableSlots", availableSlots);
        metrics.put("fullyBookedSlots", fullyBookedSlots);
        metrics.put("averageOccupancy", String.format("%.2f%%", avgOccupancy));

        return metrics;
    }

    private Map<String, Object> generateSlotOccupancyReport(List<TimeSlot> slots) {
        Map<String, Object> report = new HashMap<>();

        if (slots.isEmpty()) {
            report.put("message", "No hay slots en el rango especificado");
            return report;
        }

        // Agrupar por punto de venta
        Map<String, List<TimeSlot>> slotsByPointOfSale = slots.stream()
                .collect(Collectors.groupingBy(TimeSlot::getPointOfSaleId));

        List<Map<String, Object>> pointOfSaleMetrics = new ArrayList<>();

        for (Map.Entry<String, List<TimeSlot>> entry : slotsByPointOfSale.entrySet()) {
            String pointId = entry.getKey();
            List<TimeSlot> pointSlots = entry.getValue();

            Map<String, Object> metrics = calculateSlotMetrics(pointSlots);
            metrics.put("pointOfSaleId", pointId);

            pointOfSaleMetrics.add(metrics);
        }

        // Métricas generales
        int totalCapacity = slots.stream()
                .mapToInt(TimeSlot::getAvailableCapacity)
                .sum();

        int totalBooked = slots.stream()
                .mapToInt(TimeSlot::getBookedCount)
                .sum();

        double overallOccupancy = totalCapacity > 0 ?
                (double) totalBooked / totalCapacity * 100 : 0.0;

        report.put("totalSlots", slots.size());
        report.put("totalCapacity", totalCapacity);
        report.put("totalBooked", totalBooked);
        report.put("overallOccupancy", String.format("%.2f%%", overallOccupancy));
        report.put("byPointOfSale", pointOfSaleMetrics);

        return report;
    }
}