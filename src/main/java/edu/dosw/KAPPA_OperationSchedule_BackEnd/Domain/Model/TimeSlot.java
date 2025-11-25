package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
    private Boolean available = true;

    public TimeSlot() {}

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, Integer availableCapacity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableCapacity = availableCapacity;
        this.available = true;
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