package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class CreateOperatingHoursCommand {
    private final String pointOfSaleId;
    private final DayOfWeek dayOfWeek;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public CreateOperatingHoursCommand(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        validateInput(pointOfSaleId, dayOfWeek, openingTime, closingTime);

        this.pointOfSaleId = pointOfSaleId;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    private void validateInput(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        if (pointOfSaleId == null || pointOfSaleId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del punto de venta es requerido");
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

    public String getPointOfSaleId() { return pointOfSaleId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getOpeningTime() { return openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
}