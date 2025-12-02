package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import java.time.LocalTime;

public class CreateCategoryScheduleRequest {
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;

    public CreateCategoryScheduleRequest() {}

    public CreateCategoryScheduleRequest(String categoryName, LocalTime startTime, LocalTime endTime) {
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}