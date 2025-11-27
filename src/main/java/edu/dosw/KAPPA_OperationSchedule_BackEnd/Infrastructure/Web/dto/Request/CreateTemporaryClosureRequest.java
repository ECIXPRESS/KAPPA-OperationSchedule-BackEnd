package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import java.time.LocalDateTime;

public class CreateTemporaryClosureRequest {
    private String pointOfSaleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;

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
}