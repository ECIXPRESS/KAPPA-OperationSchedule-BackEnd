package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManageTemporaryClosuresUseCase {

    private final TemporaryClosureRepositoryPort temporaryClosureRepository;

    public ManageTemporaryClosuresUseCase(TemporaryClosureRepositoryPort temporaryClosureRepository) {
        this.temporaryClosureRepository = temporaryClosureRepository;
    }

    public TemporaryClosure createTemporaryClosure(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        TemporaryClosure closure = new TemporaryClosure(pointOfSaleId, startDateTime, endDateTime, reason);
        return temporaryClosureRepository.save(closure);
    }

    public List<TemporaryClosure> getActiveClosuresByPointOfSale(String pointOfSaleId, LocalDateTime dateTime) {
        return temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, dateTime);
    }

    public List<TemporaryClosure> getClosuresByPointOfSale(String pointOfSaleId) {
        return temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);
    }
}