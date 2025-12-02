package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;

public class ToggleTemporaryClosureStatusCommand {
    private final String id;
    private final Boolean active;

    public ToggleTemporaryClosureStatusCommand(String id, Boolean active) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del cierre temporal es requerido");
        }

        if (active == null) {
            throw BusinessException.validationError("El estado (active) es requerido");
        }

        this.id = id;
        this.active = active;
    }

    public String getId() { return id; }
    public Boolean getActive() { return active; }
}