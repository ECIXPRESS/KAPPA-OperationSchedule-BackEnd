package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Component
public class OperatingHoursRepositoryAdapter implements OperatingHoursRepositoryPort {

    private final OperatingHoursRepository operatingHoursRepository;

    public OperatingHoursRepositoryAdapter(OperatingHoursRepository operatingHoursRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
    }

    @Override
    public OperatingHours save(OperatingHours operatingHours) {
        return operatingHoursRepository.save(operatingHours);
    }

    @Override
    public Optional<OperatingHours> findById(String id) {
        return operatingHoursRepository.findById(id);
    }

    @Override
    public List<OperatingHours> findByPointOfSaleId(String pointOfSaleId) {
        return operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
    }

    @Override
    public List<OperatingHours> findByPointOfSaleIdAndDayOfWeek(String pointOfSaleId, DayOfWeek dayOfWeek) {
        return operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek);
    }

    @Override
    public List<OperatingHours> findActiveByPointOfSaleId(String pointOfSaleId) {
        return operatingHoursRepository.findByActiveTrue().stream()
                .filter(oh -> oh.getPointOfSaleId().equals(pointOfSaleId))
                .toList();
    }

    @Override
    public void deleteById(String id) {
        operatingHoursRepository.deleteById(id);
    }

    @Override
    public List<OperatingHours> findAll() {
        return operatingHoursRepository.findAll();
    }

    @Override
    public List<OperatingHours> findAllActive() {
        return operatingHoursRepository.findByActiveTrue();
    }
}