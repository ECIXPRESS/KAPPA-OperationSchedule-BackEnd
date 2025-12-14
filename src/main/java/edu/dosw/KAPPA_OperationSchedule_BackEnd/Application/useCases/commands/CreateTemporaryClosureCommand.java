package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateTemporaryClosureCommand {
    private final String pointOfSaleId;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String reason;

    public CreateTemporaryClosureCommand(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        validateInput(pointOfSaleId, startDateTime, endDateTime, reason);

        this.pointOfSaleId = pointOfSaleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reason = reason;
    }

    private void validateInput(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        if (pointOfSaleId == null || pointOfSaleId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del punto de venta es requerido");
        }

        if (startDateTime == null || endDateTime == null) {
            throw BusinessException.validationError("Las fechas de inicio y fin son requeridas");
        }

        if (startDateTime.isAfter(endDateTime)) {
            throw BusinessException.validationError("La fecha de inicio no puede ser después de la fecha de fin");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw BusinessException.validationError("La razón del cierre temporal es requerida");
        }

        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw BusinessException.validationError("No se puede crear un cierre temporal en el pasado");
        }
    }

}