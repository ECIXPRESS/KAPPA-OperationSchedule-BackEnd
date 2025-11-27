package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Utils.IdGenerator;

@Document(collection = "temporary_closures")
public class TemporaryClosure {
    @Id
    private String id;
    private String pointOfSaleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;
    private TemporaryClosureType closureType = TemporaryClosureType.MAINTENANCE;
    private Boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    public TemporaryClosure() {}

    public TemporaryClosure(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        this.id = IdGenerator.generateId("TC");
        this.pointOfSaleId = pointOfSaleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reason = reason;
        this.active = true;
        this.createdAt = LocalDateTime.now();
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