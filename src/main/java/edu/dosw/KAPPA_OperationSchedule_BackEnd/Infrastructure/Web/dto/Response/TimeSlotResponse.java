package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import java.time.LocalDateTime;

public class TimeSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
    private Boolean available;

    public static TimeSlotResponse fromDomain(TimeSlot domain) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setStartTime(domain.getStartTime());
        response.setEndTime(domain.getEndTime());
        response.setAvailableCapacity(domain.getAvailableCapacity());
        response.setAvailable(domain.getAvailable());
        return response;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}