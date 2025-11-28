package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import java.time.LocalDateTime;

public class UpdateTemporaryClosureCommand {
    private final String id;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String reason;

    public UpdateTemporaryClosureCommand(String id, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        validateInput(id, startDateTime, endDateTime, reason);

        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reason = reason;
    }

    private void validateInput(String id, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del cierre temporal es requerido");
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
    }

    public String getId() { return id; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public String getReason() { return reason; }
}