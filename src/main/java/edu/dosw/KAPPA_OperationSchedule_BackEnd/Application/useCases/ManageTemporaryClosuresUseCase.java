package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.CreateTemporaryClosureCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.UpdateTemporaryClosureCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.ToggleTemporaryClosureStatusCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManageTemporaryClosuresUseCase {

    private final TemporaryClosureRepositoryPort temporaryClosureRepository;

    public ManageTemporaryClosuresUseCase(TemporaryClosureRepositoryPort temporaryClosureRepository) {
        this.temporaryClosureRepository = temporaryClosureRepository;
    }

    // ========== COMMAND METHODS ========== //

    /**
     * Crea un nuevo cierre temporal
     */
    public TemporaryClosure execute(CreateTemporaryClosureCommand command) {
        // Las validaciones básicas ya están en el Command
        TemporaryClosure closure = new TemporaryClosure(
                command.getPointOfSaleId(),
                command.getStartDateTime(),
                command.getEndDateTime(),
                command.getReason()
        );
        return temporaryClosureRepository.save(closure);
    }

    /**
     * Actualiza un cierre temporal existente
     */
    public TemporaryClosure execute(UpdateTemporaryClosureCommand command) {
        TemporaryClosure existing = temporaryClosureRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.validationError("Cierre temporal no encontrado con ID: " + command.getId()));

        existing.setStartDateTime(command.getStartDateTime());
        existing.setEndDateTime(command.getEndDateTime());
        existing.setReason(command.getReason());

        return temporaryClosureRepository.save(existing);
    }

    /**
     * Activa/desactiva un cierre temporal
     */
    public TemporaryClosure execute(ToggleTemporaryClosureStatusCommand command) {
        TemporaryClosure closure = temporaryClosureRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.validationError("Cierre temporal no encontrado con ID: " + command.getId()));

        closure.setActive(command.getActive());
        return temporaryClosureRepository.save(closure);
    }

    // ========== QUERY METHODS ========== //

    /**
     * Obtiene cierres activos por punto de venta y fecha/hora específica
     */
    public List<TemporaryClosure> getActiveClosuresByPointOfSale(String pointOfSaleId, LocalDateTime dateTime) {
        List<TemporaryClosure> closures = temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);
        if (closures.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }
        return temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, dateTime);
    }

    /**
     * Obtiene todos los cierres de un punto de venta
     */
    public List<TemporaryClosure> getClosuresByPointOfSale(String pointOfSaleId) {
        List<TemporaryClosure> closures = temporaryClosureRepository.findByPointOfSaleId(pointOfSaleId);
        if (closures.isEmpty()) {
            throw BusinessException.pointOfSaleNotFound(pointOfSaleId);
        }
        return closures;
    }

    /**
     * Obtiene todos los cierres temporales del sistema
     */
    public List<TemporaryClosure> getAllTemporaryClosures() {
        List<TemporaryClosure> allClosures = temporaryClosureRepository.findAll();
        if (allClosures.isEmpty()) {
            throw BusinessException.validationError("No hay cierres temporales configurados en el sistema");
        }
        return allClosures;
    }

    /**
     * Obtiene cierres activos dentro de un rango de fechas
     */
    public List<TemporaryClosure> getActiveClosuresInRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha de fin");
        }

        List<TemporaryClosure> closures = temporaryClosureRepository.findActiveClosuresInRange(start, end);
        if (closures.isEmpty()) {
            throw BusinessException.validationError("No hay cierres temporales activos en el rango especificado");
        }
        return closures;
    }

    /**
     * Elimina un cierre temporal por ID
     */
    public void deleteTemporaryClosure(String id) {
        boolean exists = temporaryClosureRepository.findById(id).isPresent();
        if (!exists) {
            throw BusinessException.validationError("Cierre temporal no encontrado con ID: " + id);
        }
        temporaryClosureRepository.deleteById(id);
    }

    // ========== MÉTODOS LEGACY (para backward compatibility) ========== //

    /**
     * @deprecated Usar execute(CreateTemporaryClosureCommand command) en su lugar
     */
    @Deprecated
    public TemporaryClosure createTemporaryClosure(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        CreateTemporaryClosureCommand command = new CreateTemporaryClosureCommand(pointOfSaleId, startDateTime, endDateTime, reason);
        return execute(command);
    }

    /**
     * @deprecated Usar execute(UpdateTemporaryClosureCommand command) en su lugar
     */
    @Deprecated
    public TemporaryClosure updateTemporaryClosure(String id, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        UpdateTemporaryClosureCommand command = new UpdateTemporaryClosureCommand(id, startDateTime, endDateTime, reason);
        return execute(command);
    }

    /**
     * @deprecated Usar execute(ToggleTemporaryClosureStatusCommand command) en su lugar
     */
    @Deprecated
    public TemporaryClosure toggleTemporaryClosureStatus(String id, Boolean active) {
        ToggleTemporaryClosureStatusCommand command = new ToggleTemporaryClosureStatusCommand(id, active);
        return execute(command);
    }
}