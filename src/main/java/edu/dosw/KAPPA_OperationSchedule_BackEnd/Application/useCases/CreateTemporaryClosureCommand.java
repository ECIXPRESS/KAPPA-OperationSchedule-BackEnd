package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import java.time.LocalDateTime;

public class CreateTemporaryClosureCommand {
    private String pointOfSaleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;

    public CreateTemporaryClosureCommand(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        this.pointOfSaleId = pointOfSaleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reason = reason;
    }

    public String getPointOfSaleId() {
        return pointOfSaleId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getReason() {
        return reason;
    }
}