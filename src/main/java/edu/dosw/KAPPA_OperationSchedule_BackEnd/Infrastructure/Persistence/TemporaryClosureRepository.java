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
            LocalDateTime dateTime, LocalDateTime dateTime2);

    @Override
    default List<TemporaryClosure> findActiveClosuresByPointOfSaleAndDateTime(String pointOfSaleId, LocalDateTime dateTime) {
        List<TemporaryClosure> pointOfSaleClosures = findByPointOfSaleId(pointOfSaleId);
        return pointOfSaleClosures.stream()
                .filter(closure -> closure.getActive() != null && closure.getActive())
                .filter(closure -> !dateTime.isBefore(closure.getStartDateTime()) &&
                        !dateTime.isAfter(closure.getEndDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    default List<TemporaryClosure> findActiveClosuresInRange(LocalDateTime start, LocalDateTime end) {
        List<TemporaryClosure> activeClosures = findByActiveTrue();
        return activeClosures.stream()
                .filter(closure -> isClosureInRange(closure, start, end))
                .collect(Collectors.toList());
    }

    private boolean isClosureInRange(TemporaryClosure closure, LocalDateTime start, LocalDateTime end) {
        LocalDateTime closureStart = closure.getStartDateTime();
        LocalDateTime closureEnd = closure.getEndDateTime();

        return (closureStart.isBefore(end) && closureEnd.isAfter(start));
    }
}