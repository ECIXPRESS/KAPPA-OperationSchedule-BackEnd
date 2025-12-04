package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.Getter;

@Getter
public class ToggleCategoryStatusCommand {
    private final String id;
    private final Boolean active;

    public ToggleCategoryStatusCommand(String id, Boolean active) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID de la categoría es requerido");
        }

        if (active == null) {
            throw BusinessException.validationError("El estado (active) es requerido");
        }

        this.id = id;
        this.active = active;
    }

}