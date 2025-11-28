package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TemporaryClosureRepositoryAdapter implements TemporaryClosureRepositoryPort {

    private final TemporaryClosureRepository temporaryClosureRepository;

    public TemporaryClosureRepositoryAdapter(TemporaryClosureRepository temporaryClosureRepository) {
        this.temporaryClosureRepository = temporaryClosureRepository;
    }

    @Override
    public TemporaryClosure save(TemporaryClosure temporaryClosure) {
        return temporaryClosureRepository.save(temporaryClosure);
    }

    @Override
    public Optional<TemporaryClosure> findById(String id) {
        return temporaryClosureRepository.findById(id);
    }

    @Override
    public List<TemporaryClosure> findByPointOfSaleId(String pointOfSaleId) {
        return temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);
    }

    @Override
    public List<TemporaryClosure> findActiveClosuresByPointOfSaleAndDateTime(String pointOfSaleId, LocalDateTime dateTime) {
        return temporaryClosureRepository.findByActiveTrueAndStartDateTimeBeforeAndEndDateTimeAfter(
                        dateTime, dateTime
                ).stream()
                .filter(closure -> closure.getPointOfSaleId().equals(pointOfSaleId))
                .toList();
    }

    @Override
    public List<TemporaryClosure> findActiveClosuresInRange(LocalDateTime start, LocalDateTime end) {
        return temporaryClosureRepository.findByActiveTrue().stream()
                .filter(closure ->
                        (closure.getStartDateTime().isBefore(end) && closure.getEndDateTime().isAfter(start))
                )
                .toList();
    }

    @Override
    public void deleteById(String id) {
        temporaryClosureRepository.deleteById(id);
    }

    @Override
    public List<TemporaryClosure> findAll() {
        return temporaryClosureRepository.findAll();
    }
}