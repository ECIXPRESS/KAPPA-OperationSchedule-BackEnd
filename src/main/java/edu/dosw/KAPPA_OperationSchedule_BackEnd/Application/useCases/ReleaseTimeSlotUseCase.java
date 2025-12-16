package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.ReleaseTimeSlotCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class ReleaseTimeSlotUseCase {

    private final TimeSlotRepositoryPort timeSlotRepository;

    public ReleaseTimeSlotUseCase(TimeSlotRepositoryPort timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot execute(ReleaseTimeSlotCommand command) {
        if (command.getSlotId() == null || command.getSlotId().isEmpty()) {
            throw BusinessException.validationError("El ID del slot es requerido");
        }

        TimeSlot slot = timeSlotRepository.findById(command.getSlotId())
                .orElseThrow(() -> BusinessException.validationError("Slot no encontrado con ID: " + command.getSlotId()));

        if (slot.getBookedCount() <= 0) {
            throw BusinessException.validationError("El slot no tiene reservas para liberar");
        }

        slot.releaseSlot();

        return timeSlotRepository.save(slot);
    }

    public TimeSlot releaseAll(String slotId) {
        TimeSlot slot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> BusinessException.validationError("Slot no encontrado"));

        if (slot.getBookedCount() > 0) {
            slot.setBookedCount(0);
            return timeSlotRepository.save(slot);
        }

        return slot;
    }
}