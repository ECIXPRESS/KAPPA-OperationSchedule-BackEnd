package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimeSlotRequest {
    private String pointOfSaleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
}