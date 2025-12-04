package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimeSlotCommand {
    private String pointOfSaleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCapacity;

    public void validate() {
        if (pointOfSaleId == null || pointOfSaleId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del punto de venta es requerido");
        }

        if (startTime == null || endTime == null) {
            throw BusinessException.validationError("Las horas de inicio y fin son requeridas");
        }

        if (startTime.isAfter(endTime)) {
            throw BusinessException.validationError("La hora de inicio no puede ser después de la hora de fin");
        }

        if (availableCapacity == null || availableCapacity <= 0) {
            throw BusinessException.validationError("La capacidad disponible debe ser mayor a 0");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw BusinessException.validationError("No se puede crear un slot en el pasado");
        }
    }
}