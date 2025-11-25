package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import java.time.LocalDateTime;
import java.util.List;

public class AvailabilityResult {
    private Boolean available;
    private String pointOfSaleId;
    private LocalDateTime requestedTime;
    private String reason;
    private String categoryMessage = "";
    private List<String> availableTimeSlots;

    public AvailabilityResult() {}

    public AvailabilityResult(Boolean available, String pointOfSaleId, LocalDateTime requestedTime, String reason) {
        this.available = available;
        this.pointOfSaleId = pointOfSaleId;
        this.requestedTime = requestedTime;
        this.reason = reason;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getPointOfSaleId() {
        return pointOfSaleId; }
    public void setPointOfSaleId(String pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
    }

    public LocalDateTime getRequestedTime() {
        return requestedTime;
    }
    public void setRequestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCategoryMessage() {
        return categoryMessage;
    }

    public void setCategoryMessage(String categoryMessage) {
        this.categoryMessage = categoryMessage;
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}