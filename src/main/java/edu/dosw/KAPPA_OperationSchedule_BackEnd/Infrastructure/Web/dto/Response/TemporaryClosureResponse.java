package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosureType;
import java.time.LocalDateTime;

public class TemporaryClosureResponse {
    private String id;
    private String pointOfSaleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;
    private TemporaryClosureType closureType;
    private Boolean active;
    private LocalDateTime createdAt;

    public static TemporaryClosureResponse fromDomain(TemporaryClosure domain) {
        TemporaryClosureResponse response = new TemporaryClosureResponse();
        response.setId(domain.getId());
        response.setPointOfSaleId(domain.getPointOfSaleId());
        response.setStartDateTime(domain.getStartDateTime());
        response.setEndDateTime(domain.getEndDateTime());
        response.setReason(domain.getReason());
        response.setClosureType(domain.getClosureType());
        response.setActive(domain.getActive());
        response.setCreatedAt(domain.getCreatedAt());
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPointOfSaleId() {
        return pointOfSaleId;
    }

    public void setPointOfSaleId(String pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TemporaryClosureType getClosureType() {
        return closureType;
    }

    public void setClosureType(TemporaryClosureType closureType) {
        this.closureType = closureType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}