package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "time_slots")
@Builder
public class TimeSlot {
    @Id
    private String id;
    private String pointOfSaleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;
    private Integer bookedCount = 0;
    private Boolean available = true;

    public TimeSlot(LocalDateTime slotStart, LocalDateTime slotEnd, int availableCapacity) {
        this.startTime = slotStart;
        this.endTime = slotEnd;
        this.availableCapacity = availableCapacity;
    }

    public TimeSlot(String pointOfSaleId, LocalDateTime startTime, LocalDateTime endTime, Integer availableCapacity) {
        this.pointOfSaleId = pointOfSaleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableCapacity = availableCapacity;
    }

    /**
     * Verifica si el slot está disponible para reservar
     */
    public boolean isAvailable() {
        return Boolean.TRUE.equals(available) && bookedCount < availableCapacity;
    }

    /**
     * Reserva un espacio en el slot (incrementa bookedCount)
     */
    public void reserveSlot() {
        if (!isAvailable()) {
            throw new BusinessException("Slot no disponible - capacidad agotada");
        }
        bookedCount++;
    }

    /**
     * Libera un espacio en el slot (decrementa bookedCount)
     */
    public void releaseSlot() {
        if (bookedCount > 0) {
            bookedCount--;
        }
    }

    /**
     * Obtiene la capacidad disponible actual
     */
    public Integer getAvailableCapacity() {
        return availableCapacity - bookedCount;
    }

    /**
     * Verifica si un tiempo dado está dentro del rango del slot
     */
    public boolean containsTime(LocalDateTime dateTime) {
        return !dateTime.isBefore(startTime) && !dateTime.isAfter(endTime);
    }

    /**
     * Verifica si el slot se solapa con otro rango de tiempo
     */
    public boolean overlapsWith(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !startTime.isAfter(otherEnd) && !endTime.isBefore(otherStart);
    }

    @Deprecated
    public void incrementBookings() {
        reserveSlot();
    }

    @Deprecated
    public void decrementBookings() {
        releaseSlot();
    }
}