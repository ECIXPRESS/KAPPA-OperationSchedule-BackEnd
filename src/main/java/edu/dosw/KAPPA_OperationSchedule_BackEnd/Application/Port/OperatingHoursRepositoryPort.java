package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface OperatingHoursRepositoryPort {
    OperatingHours save(OperatingHours operatingHours);
    Optional<OperatingHours> findById(String id);
    List<OperatingHours> findByPointOfSaleId(String pointOfSaleId);
    List<OperatingHours> findByPointOfSaleIdAndDayOfWeek(String pointOfSaleId, DayOfWeek dayOfWeek);
    List<OperatingHours> findActiveByPointOfSaleId(String pointOfSaleId);
    void deleteById(String id);
    List<OperatingHours> findAll();
}