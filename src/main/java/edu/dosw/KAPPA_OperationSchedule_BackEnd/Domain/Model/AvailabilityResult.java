package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AvailabilityResult {
    private Boolean available;
    private String pointOfSaleId;
    private LocalDateTime requestedTime;
    private String reason;
    private String categoryMessage = "";
    private List<String> availableTimeSlots;

    public AvailabilityResult(boolean b, String pointOfSaleId, LocalDateTime requestedTime, String puntoDeVentaCerradoTemporalmente) {
        this.available = b;
        this.pointOfSaleId = pointOfSaleId;
        this.requestedTime = requestedTime;
        this.reason = puntoDeVentaCerradoTemporalmente;
    }
}