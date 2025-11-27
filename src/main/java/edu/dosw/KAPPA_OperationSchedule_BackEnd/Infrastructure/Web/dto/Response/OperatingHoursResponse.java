package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class OperatingHoursResponse {
    private String id;
    private String pointOfSaleId;
    private DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean active;

    public static OperatingHoursResponse fromDomain(OperatingHours domain) {
        OperatingHoursResponse response = new OperatingHoursResponse();
        response.setId(domain.getId());
        response.setPointOfSaleId(domain.getPointOfSaleId());
        response.setDayOfWeek(domain.getDayOfWeek());
        response.setOpeningTime(domain.getOpeningTime());
        response.setClosingTime(domain.getClosingTime());
        response.setActive(domain.getActive());
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}