package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TemporaryClosureRepositoryPort {
    TemporaryClosure save(TemporaryClosure temporaryClosure);
    Optional<TemporaryClosure> findById(String id);
    List<TemporaryClosure> findByPointOfSaleId(String pointOfSaleId);
    List<TemporaryClosure> findActiveClosuresByPointOfSaleAndDateTime(String pointOfSaleId, LocalDateTime dateTime);
    List<TemporaryClosure> findActiveClosuresInRange(LocalDateTime start, LocalDateTime end);
    void deleteById(String id);
    List<TemporaryClosure> findAll();
}