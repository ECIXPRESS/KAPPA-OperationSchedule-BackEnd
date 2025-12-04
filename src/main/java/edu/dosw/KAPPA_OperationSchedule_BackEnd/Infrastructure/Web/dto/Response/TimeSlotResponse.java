package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private String id;
    private String pointOfSaleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
    private Integer bookedCount;
    private Boolean available;

    public static TimeSlotResponse fromDomain(TimeSlot domain) {
        return TimeSlotResponse.builder()
                .id(domain.getId())
                .pointOfSaleId(domain.getPointOfSaleId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .availableCapacity(domain.getAvailableCapacity())
                .bookedCount(domain.getBookedCount())
                .available(domain.getAvailable())
                .build();
    }
}