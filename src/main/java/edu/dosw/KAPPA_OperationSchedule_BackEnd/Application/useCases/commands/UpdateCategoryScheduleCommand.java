package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateCategoryScheduleCommand {
    private final String id;
    private final String categoryName;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public UpdateCategoryScheduleCommand(String id, String categoryName, LocalTime startTime, LocalTime endTime) {
        validateInput(id, categoryName, startTime, endTime);

        this.id = id;
        this.categoryName = categoryName.trim();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateInput(String id, String categoryName, LocalTime startTime, LocalTime endTime) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID de la categoría es requerido");
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw BusinessException.validationError("El nombre de la categoría es requerido");
        }

        if (startTime == null || endTime == null) {
            throw BusinessException.validationError("Las horas de inicio y fin son requeridas");
        }

        if (startTime.isAfter(endTime)) {
            throw BusinessException.validationError("La hora de inicio no puede ser después de la hora de fin");
        }
    }

}