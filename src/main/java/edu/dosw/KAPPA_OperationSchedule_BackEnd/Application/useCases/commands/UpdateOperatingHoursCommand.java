package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class UpdateOperatingHoursCommand {
    private final String id;
    private final DayOfWeek dayOfWeek;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public UpdateOperatingHoursCommand(String id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        validateInput(id, dayOfWeek, openingTime, closingTime);

        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    private void validateInput(String id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        if (id == null || id.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del horario es requerido");
        }

        if (dayOfWeek == null) {
            throw BusinessException.validationError("El día de la semana es requerido");
        }

        if (openingTime == null || closingTime == null) {
            throw BusinessException.validationError("Las horas de apertura y cierre son requeridas");
        }

        if (openingTime.isAfter(closingTime)) {
            throw BusinessException.validationError("La hora de apertura no puede ser después de la hora de cierre");
        }
    }

    public String getId() { return id; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getOpeningTime() { return openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
}