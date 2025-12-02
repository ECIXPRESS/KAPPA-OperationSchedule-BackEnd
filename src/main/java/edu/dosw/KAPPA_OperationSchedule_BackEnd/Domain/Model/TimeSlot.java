package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
    private Boolean available = true;

    public TimeSlot(LocalDateTime slotStart, LocalDateTime slotEnd, int i) {
        this.startTime = slotStart;
        this.endTime = slotEnd;
        this.availableCapacity = i;
    }
}