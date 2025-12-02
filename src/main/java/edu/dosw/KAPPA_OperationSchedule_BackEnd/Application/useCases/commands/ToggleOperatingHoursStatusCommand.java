package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;

public class ToggleOperatingHoursStatusCommand {
    private final String id;
    private final Boolean active;

    public ToggleOperatingHoursStatusCommand(String id, Boolean active) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del horario es requerido");
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