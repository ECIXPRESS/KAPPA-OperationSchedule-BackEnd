package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTimeSlotsCommand {
    private String pointOfSaleId;
    private LocalDate date;
    private Integer slotDurationMinutes = 30;
    private Integer defaultCapacity = 10;

    public void validate() {
        if (pointOfSaleId == null || pointOfSaleId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del punto de venta es requerido");
        }

        if (date == null) {
            throw BusinessException.validationError("La fecha es requerida");
        }

        if (date.isBefore(LocalDate.now())) {
            throw BusinessException.validationError("No se pueden generar slots para fechas pasadas");
        }

        if (slotDurationMinutes == null || slotDurationMinutes <= 0) {
            throw BusinessException.validationError("La duración del slot debe ser mayor a 0 minutos");
        }

        if (defaultCapacity == null || defaultCapacity <= 0) {
            throw BusinessException.validationError("La capacidad por defecto debe ser mayor a 0");
        }
    }
}