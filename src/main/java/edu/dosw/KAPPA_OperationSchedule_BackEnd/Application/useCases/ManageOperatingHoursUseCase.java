package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
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
        if (openingTime.isAfter(closingTime)) {
            throw BusinessException.validationError("La hora de apertura no puede ser después de la hora de cierre");
        }

        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        return operatingHoursRepository.save(operatingHours);
    }

    public List<OperatingHours> getOperatingHoursByPointOfSale(String pointOfSaleId) {
        List<OperatingHours> hours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        if (hours.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }
        return hours;
    }

    public List<OperatingHours> getOperatingHoursByPointOfSaleAndDay(String pointOfSaleId, DayOfWeek dayOfWeek) {
        List<OperatingHours> hours = operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek);
        if (hours.isEmpty()) {
            throw BusinessException.validationError("No se encontraron horarios para el punto de venta " + pointOfSaleId + " en " + dayOfWeek);
        }
        return hours;
    }

    public void deleteOperatingHours(String id) {
        boolean exists = operatingHoursRepository.findById(id).isPresent();
        if (!exists) {
            throw BusinessException.scheduleNotFound(id);
        }
        operatingHoursRepository.deleteById(id);
    }
}