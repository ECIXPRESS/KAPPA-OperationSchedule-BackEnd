package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleReportsUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;

    public ScheduleReportsUseCase(OperatingHoursRepositoryPort operatingHoursRepository, TemporaryClosureRepositoryPort temporaryClosureRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
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
}