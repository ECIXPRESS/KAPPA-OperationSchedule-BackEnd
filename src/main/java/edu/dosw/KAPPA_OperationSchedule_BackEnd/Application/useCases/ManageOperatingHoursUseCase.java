package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class ManageOperatingHoursUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;

    public ManageOperatingHoursUseCase(OperatingHoursRepositoryPort operatingHoursRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
    }

    public OperatingHours createOperatingHours(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        return operatingHoursRepository.save(operatingHours);
    }

    public List<OperatingHours> getOperatingHoursByPointOfSale(String pointOfSaleId) {
        return operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
    }

    public List<OperatingHours> getOperatingHoursByPointOfSaleAndDay(String pointOfSaleId, DayOfWeek dayOfWeek) {
        return operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek);
    }

    public void deleteOperatingHours(String id) {
        operatingHoursRepository.deleteById(id);
    }
}