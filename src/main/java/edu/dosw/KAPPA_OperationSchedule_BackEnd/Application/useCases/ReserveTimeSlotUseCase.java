package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.ReserveTimeSlotCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReserveTimeSlotUseCase {

    private final TimeSlotRepositoryPort timeSlotRepository;

    public ReserveTimeSlotUseCase(TimeSlotRepositoryPort timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot execute(ReserveTimeSlotCommand command) {
        if (command.getSlotId() == null || command.getSlotId().isEmpty()) {
            throw BusinessException.validationError("El ID del slot es requerido");
        }

        if (command.getOrderId() == null || command.getOrderId().isEmpty()) {
            throw BusinessException.validationError("El ID del pedido es requerido");
        }

        TimeSlot slot = timeSlotRepository.findById(command.getSlotId())
                .orElseThrow(() -> BusinessException.validationError("Slot no encontrado con ID: " + command.getSlotId()));

        if (!slot.isAvailable()) {
            throw BusinessException.validationError(
                    "El slot no está disponible. Capacidad: " +
                            slot.getBookedCount() + "/" + slot.getAvailableCapacity()
            );
        }

        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw BusinessException.validationError("No se puede reservar un slot en el pasado");
        }

        slot.reserveSlot();
        return timeSlotRepository.save(slot);
    }

    public TimeSlot reserveByTime(String pointOfSaleId, LocalDateTime startTime,
                                  LocalDateTime endTime, String orderId) {
        TimeSlot slot = timeSlotRepository
                .findByPointOfSaleIdAndSlot(pointOfSaleId, startTime, endTime)
                .orElseThrow(() -> BusinessException.validationError(
                        "No se encontró slot para el intervalo especificado"
                ));

        ReserveTimeSlotCommand command = new ReserveTimeSlotCommand();
        command.setSlotId(slot.getId());
        command.setOrderId(orderId);

        return execute(command);
    }
}