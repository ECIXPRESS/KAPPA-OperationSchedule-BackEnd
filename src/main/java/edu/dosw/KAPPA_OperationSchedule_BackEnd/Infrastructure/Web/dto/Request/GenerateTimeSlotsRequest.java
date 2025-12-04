package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTimeSlotsRequest {
    private String pointOfSaleId;
    private LocalDate date;
    private Integer slotDurationMinutes = 30;
    private Integer defaultCapacity = 10;
}