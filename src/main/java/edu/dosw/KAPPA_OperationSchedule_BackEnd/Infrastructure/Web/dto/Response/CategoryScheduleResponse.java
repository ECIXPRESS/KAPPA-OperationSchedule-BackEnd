package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import java.time.LocalTime;

public class CategoryScheduleResponse {
    private String id;
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active;

    public static CategoryScheduleResponse fromDomain(CategorySchedule domain) {
        CategoryScheduleResponse response = new CategoryScheduleResponse();
        response.setId(domain.getId());
        response.setCategoryName(domain.getCategoryName());
        response.setStartTime(domain.getStartTime());
        response.setEndTime(domain.getEndTime());
        response.setActive(domain.getActive());
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}