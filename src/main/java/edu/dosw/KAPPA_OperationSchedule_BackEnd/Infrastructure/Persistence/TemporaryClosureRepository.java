package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemporaryClosureRepository extends MongoRepository<TemporaryClosure, String> {
    List<TemporaryClosure> findByPointOfSaleId(String pointOfSaleId);

    @Query("{ 'pointOfSaleId': ?0, 'active': true, 'startDateTime': { $lte: ?1 }, 'endDateTime': { $gte: ?1 } }")
    List<TemporaryClosure> findActiveClosuresByPointOfSaleAndDateTime(String pointOfSaleId, LocalDateTime dateTime);

    @Query("{ 'active': true, 'startDateTime': { $lte: ?1 }, 'endDateTime': { $gte: ?0 } }")
    List<TemporaryClosure> findActiveClosuresInRange(LocalDateTime start, LocalDateTime end);

    List<TemporaryClosure> findByActiveTrue();
}