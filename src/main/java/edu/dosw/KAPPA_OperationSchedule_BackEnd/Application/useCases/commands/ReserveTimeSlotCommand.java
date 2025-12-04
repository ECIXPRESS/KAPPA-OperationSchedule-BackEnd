package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveTimeSlotCommand {
    private String slotId;
    private String orderId;
    private String userId;

    public void validate() {
        if (slotId == null || slotId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del slot es requerido");
        }

        if (orderId == null || orderId.trim().isEmpty()) {
            throw BusinessException.validationError("El ID del pedido es requerido");
        }
    }
}