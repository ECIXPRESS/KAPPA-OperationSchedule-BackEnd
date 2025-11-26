package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class CreateOperatingHoursCommand {
    private String pointOfSaleId;
    private DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public CreateOperatingHoursCommand(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        this.pointOfSaleId = pointOfSaleId;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public String getPointOfSaleId() {
        return pointOfSaleId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

}