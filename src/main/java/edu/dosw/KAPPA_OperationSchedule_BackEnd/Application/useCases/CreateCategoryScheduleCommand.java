package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import java.time.LocalTime;

public class CreateCategoryScheduleCommand {
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;

    public CreateCategoryScheduleCommand(String categoryName, LocalTime startTime, LocalTime endTime) {
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.endTime = endTime;
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