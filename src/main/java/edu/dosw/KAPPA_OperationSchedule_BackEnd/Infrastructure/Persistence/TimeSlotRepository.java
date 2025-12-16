package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {
    List<TimeSlot> findByPointOfSaleId(String pointOfSaleId);
    List<TimeSlot> findByPointOfSaleIdAndStartTimeBetween(
            String pointOfSaleId,
            LocalDateTime start,
            LocalDateTime end
    );
    Optional<TimeSlot> findByPointOfSaleIdAndStartTimeAndEndTime(
            String pointOfSaleId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
    List<TimeSlot> findByAvailableTrue();

    List<TimeSlot> findByPointOfSaleIdAndAvailableTrue(String pointOfSaleId);
    List<TimeSlot> findByPointOfSaleIdAndStartTimeAfter(String pointOfSaleId, LocalDateTime dateTime);
    List<TimeSlot> findByPointOfSaleIdAndBookedCountLessThan(
            String pointOfSaleId, Integer bookedCount);
}