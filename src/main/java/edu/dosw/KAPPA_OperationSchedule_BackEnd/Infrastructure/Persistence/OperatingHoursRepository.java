package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface OperatingHoursRepository extends MongoRepository<OperatingHours, String>, OperatingHoursRepositoryPort {

    List<OperatingHours> findByPointOfSaleId(String pointOfSaleId);
    List<OperatingHours> findByPointOfSaleIdAndDayOfWeek(String pointOfSaleId, DayOfWeek dayOfWeek);
    List<OperatingHours> findByActiveTrue();

    @Override
    default List<OperatingHours> findActiveByPointOfSaleId(String pointOfSaleId) {
        List<OperatingHours> allHours = findByPointOfSaleId(pointOfSaleId);
        return allHours.stream()
                .filter(hours -> hours.getActive() != null && hours.getActive())
                .collect(Collectors.toList());
    }
}