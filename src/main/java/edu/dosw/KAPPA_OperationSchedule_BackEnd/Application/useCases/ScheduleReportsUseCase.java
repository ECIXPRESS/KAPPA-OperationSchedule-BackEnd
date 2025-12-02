package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleReportsUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;
    private final CategoryScheduleRepositoryPort categoryScheduleRepository;

    public ScheduleReportsUseCase(
            OperatingHoursRepositoryPort operatingHoursRepository,
            TemporaryClosureRepositoryPort temporaryClosureRepository,
            CategoryScheduleRepositoryPort categoryScheduleRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
        this.categoryScheduleRepository = categoryScheduleRepository;
    }

    public Map<String, Object> generatePointOfSaleReport(String pointOfSaleId) {
        List<OperatingHours> operatingHours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        if (operatingHours.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }

        Map<String, Object> report = new HashMap<>();

        List<OperatingHours> allOperatingHours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        List<TemporaryClosure> closures = temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);

        report.put("pointOfSaleId", pointOfSaleId);
        report.put("operatingHours", allOperatingHours);
        report.put("temporaryClosures", closures);
        report.put("closureCount", closures.size());

        return report;
    }

    public Map<String, Object> generateAvailabilityReport(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha de fin");
        }

        Map<String, Object> report = new HashMap<>();

        List<OperatingHours> allOperatingHours = operatingHoursRepository.findAllActive();
        List<TemporaryClosure> closuresInRange = temporaryClosureRepository.findActiveClosuresInRange(start, end);

        report.put("reportPeriod", Map.of("start", start, "end", end));
        report.put("totalOperatingHours", allOperatingHours.size());
        report.put("closuresInPeriod", closuresInRange.size());
        report.put("closuresDetails", closuresInRange);

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
}