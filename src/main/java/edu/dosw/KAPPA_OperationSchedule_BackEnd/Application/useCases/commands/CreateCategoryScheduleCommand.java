package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import java.time.LocalTime;

public class CreateCategoryScheduleCommand {
    private final String categoryName;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public CreateCategoryScheduleCommand(String categoryName, LocalTime startTime, LocalTime endTime) {
        validateInput(categoryName, startTime, endTime);

        this.categoryName = categoryName.trim();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateInput(String categoryName, LocalTime startTime, LocalTime endTime) {
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

    public String getCategoryName() {
        return categoryName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}