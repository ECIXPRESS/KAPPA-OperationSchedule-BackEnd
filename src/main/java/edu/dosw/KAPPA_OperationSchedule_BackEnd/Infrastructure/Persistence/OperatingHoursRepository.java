package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface OperatingHoursRepository extends MongoRepository<OperatingHours, String> {

    List<OperatingHours> findByPointOfSaleId(String pointOfSaleId);
    List<OperatingHours> findByPointOfSaleIdAndDayOfWeek(String pointOfSaleId, DayOfWeek dayOfWeek);
    List<OperatingHours> findByActiveTrue();
}