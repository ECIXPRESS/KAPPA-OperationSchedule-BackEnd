package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
import java.time.LocalDateTime;
import java.util.List;

public class AvailabilityResponse {
    private Boolean available;
    private String pointOfSaleId;
    private LocalDateTime requestedTime;
    private String reason;
    private String categoryMessage;
    private List<String> availableTimeSlots;

    public static AvailabilityResponse fromDomain(AvailabilityResult domain) {
        AvailabilityResponse response = new AvailabilityResponse();
        response.setAvailable(domain.getAvailable());
        response.setPointOfSaleId(domain.getPointOfSaleId());
        response.setRequestedTime(domain.getRequestedTime());
        response.setReason(domain.getReason());
        response.setCategoryMessage(domain.getCategoryMessage());
        response.setAvailableTimeSlots(domain.getAvailableTimeSlots());
        return response;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getPointOfSaleId() {
        return pointOfSaleId;
    }

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