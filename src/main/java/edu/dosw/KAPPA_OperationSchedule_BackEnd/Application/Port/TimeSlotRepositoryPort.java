package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepositoryPort {
    TimeSlot save(TimeSlot timeSlot);
    Optional<TimeSlot> findById(String id);
    List<TimeSlot> findByPointOfSaleId(String pointOfSaleId);
    List<TimeSlot> findByPointOfSaleIdAndDateTimeRange(
            String pointOfSaleId,
            LocalDateTime start,
            LocalDateTime end
    );
    Optional<TimeSlot> findByPointOfSaleIdAndSlot(
            String pointOfSaleId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
    void deleteById(String id);
    List<TimeSlot> findAll();
}