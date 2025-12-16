package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderAvailabilityRequest {
    private String pointOfSaleId;
    private LocalDateTime requestedTime;
    private List<String> productCategories;
}