package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.CreateOperatingHoursCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.UpdateOperatingHoursCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.ToggleOperatingHoursStatusCommand;
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

    // ========== COMMAND METHODS ========== //

    /**
     * Crea nuevos horarios de operación
     */
    public OperatingHours execute(CreateOperatingHoursCommand command) {
        // Las validaciones básicas ya están en el Command
        // Solo lógica de negocio específica aquí si es necesario

        OperatingHours operatingHours = new OperatingHours(
                command.getPointOfSaleId(),
                command.getDayOfWeek(),
                command.getOpeningTime(),
                command.getClosingTime()
        );
        return operatingHoursRepository.save(operatingHours);
    }

    /**
     * Actualiza horarios de operación existentes
     */
    public OperatingHours execute(UpdateOperatingHoursCommand command) {
        OperatingHours existing = operatingHoursRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.scheduleNotFound(command.getId()));

        existing.setDayOfWeek(command.getDayOfWeek());
        existing.setOpeningTime(command.getOpeningTime());
        existing.setClosingTime(command.getClosingTime());

        return operatingHoursRepository.save(existing);
    }

    /**
     * Activa/desactiva horarios de operación
     */
    public OperatingHours execute(ToggleOperatingHoursStatusCommand command) {
        OperatingHours operatingHours = operatingHoursRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.scheduleNotFound(command.getId()));

        operatingHours.setActive(command.getActive());
        return operatingHoursRepository.save(operatingHours);
    }

    // ========== QUERY METHODS ========== //

    /**
     * Obtiene todos los horarios de un punto de venta (activos e inactivos)
     */
    public List<OperatingHours> getOperatingHoursByPointOfSale(String pointOfSaleId) {
        List<OperatingHours> hours = operatingHoursRepository.findByPointOfSaleId(pointOfSaleId);
        if (hours.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }
        return hours;
    }

    /**
     * Obtiene solo los horarios activos de un punto de venta
     */
    public List<OperatingHours> getActiveOperatingHoursByPointOfSale(String pointOfSaleId) {
        List<OperatingHours> hours = operatingHoursRepository.findActiveByPointOfSaleId(pointOfSaleId);
        if (hours.isEmpty()) {
            throw BusinessException.validationError("No hay horarios activos para el punto de venta: " + pointOfSaleId);
        }
        return hours;
    }

    /**
     * Obtiene horarios por punto de venta y día específico
     */
    public List<OperatingHours> getOperatingHoursByPointOfSaleAndDay(String pointOfSaleId, DayOfWeek dayOfWeek) {
        List<OperatingHours> hours = operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek);
        if (hours.isEmpty()) {
            throw BusinessException.validationError("No se encontraron horarios para el punto de venta " + pointOfSaleId + " en " + dayOfWeek);
        }
        return hours;
    }

    /**
     * Obtiene todos los horarios del sistema
     */
    public List<OperatingHours> getAllOperatingHours() {
        List<OperatingHours> allHours = operatingHoursRepository.findAll();
        if (allHours.isEmpty()) {
            throw BusinessException.validationError("No hay horarios de operación configurados en el sistema");
        }
        return allHours;
    }

    /**
     * Obtiene todos los horarios activos del sistema
     */
    public List<OperatingHours> getAllActiveOperatingHours() {
        List<OperatingHours> activeHours = operatingHoursRepository.findAllActive();
        if (activeHours.isEmpty()) {
            throw BusinessException.validationError("No hay horarios de operación activos configurados");
        }
        return activeHours;
    }

    /**
     * Elimina horarios de operación por ID
     */
    public void deleteOperatingHours(String id) {
        boolean exists = operatingHoursRepository.findById(id).isPresent();
        if (!exists) {
            throw BusinessException.scheduleNotFound(id);
        }
        operatingHoursRepository.deleteById(id);
    }

    // ========== MÉTODOS LEGACY (para backward compatibility) ========== //

    /**
     * @deprecated Usar execute(CreateOperatingHoursCommand command) en su lugar
     */
    @Deprecated
    public OperatingHours createOperatingHours(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        CreateOperatingHoursCommand command = new CreateOperatingHoursCommand(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        return execute(command);
    }

    /**
     * @deprecated Usar execute(UpdateOperatingHoursCommand command) en su lugar
     */
    @Deprecated
    public OperatingHours updateOperatingHours(String id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        UpdateOperatingHoursCommand command = new UpdateOperatingHoursCommand(id, dayOfWeek, openingTime, closingTime);
        return execute(command);
    }

    /**
     * @deprecated Usar execute(ToggleOperatingHoursStatusCommand command) en su lugar
     */
    @Deprecated
    public OperatingHours toggleOperatingHoursStatus(String id, Boolean active) {
        ToggleOperatingHoursStatusCommand command = new ToggleOperatingHoursStatusCommand(id, active);
        return execute(command);
    }
}