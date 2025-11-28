package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemporaryClosureRepository extends MongoRepository<TemporaryClosure, String> {

    List<TemporaryClosure> findByPointOfSaleId(String pointOfSaleId);
    List<TemporaryClosure> findByActiveTrue();
    List<TemporaryClosure> findByActiveTrueAndStartDateTimeBeforeAndEndDateTimeAfter(
            LocalDateTime endDateTime, LocalDateTime startDateTime);
}