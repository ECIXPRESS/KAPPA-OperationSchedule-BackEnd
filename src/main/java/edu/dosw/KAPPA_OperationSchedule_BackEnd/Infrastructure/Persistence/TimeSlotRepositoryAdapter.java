package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TimeSlotRepositoryAdapter implements TimeSlotRepositoryPort {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotRepositoryAdapter(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public Optional<TimeSlot> findById(String id) {
        return timeSlotRepository.findById(id);
    }

    @Override
    public List<TimeSlot> findByPointOfSaleId(String pointOfSaleId) {
        return timeSlotRepository.findByPointOfSaleId(pointOfSaleId);
    }

    @Override
    public List<TimeSlot> findByPointOfSaleIdAndDateTimeRange(
            String pointOfSaleId, LocalDateTime start, LocalDateTime end) {
        return timeSlotRepository.findByPointOfSaleIdAndStartTimeBetween(pointOfSaleId, start, end);
    }

    @Override
    public Optional<TimeSlot> findByPointOfSaleIdAndSlot(
            String pointOfSaleId, LocalDateTime startTime, LocalDateTime endTime) {
        return timeSlotRepository.findByPointOfSaleIdAndStartTimeAndEndTime(
                pointOfSaleId, startTime, endTime);
    }

    @Override
    public void deleteById(String id) {
        timeSlotRepository.deleteById(id);
    }

    @Override
    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }
}