package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import java.time.LocalDateTime;

public class AvailabilityCheckRequest {
    private String pointOfSaleId;
    private LocalDateTime requestedTime;
    private String productCategory;

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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}