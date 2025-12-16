package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TimeSlotRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.CreateTimeSlotCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CreateTimeSlotUseCase {

    private final TimeSlotRepositoryPort timeSlotRepository;

    public CreateTimeSlotUseCase(TimeSlotRepositoryPort timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot execute(CreateTimeSlotCommand command) {
        if (command.getStartTime().isBefore(LocalDateTime.now())) {
            throw BusinessException.validationError("No se puede crear un slot en el pasado");
        }

        if (command.getEndTime().isBefore(command.getStartTime())) {
            throw BusinessException.validationError("La hora de fin debe ser después de la hora de inicio");
        }

        if (command.getAvailableCapacity() == null || command.getAvailableCapacity() <= 0) {
            throw BusinessException.validationError("La capacidad disponible debe ser mayor a 0");
        }

        boolean hasOverlap = timeSlotRepository
                .findByPointOfSaleIdAndDateTimeRange(
                        command.getPointOfSaleId(),
                        command.getStartTime(),
                        command.getEndTime()
                ).stream()
                .anyMatch(existingSlot ->
                        existingSlot.getStartTime().isBefore(command.getEndTime()) &&
                                existingSlot.getEndTime().isAfter(command.getStartTime())
                );

        if (hasOverlap) {
            throw BusinessException.validationError("El slot se solapa con un slot existente");
        }

        TimeSlot slot = TimeSlot.builder()
                .pointOfSaleId(command.getPointOfSaleId())
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .availableCapacity(command.getAvailableCapacity())
                .bookedCount(0)
                .available(true)
                .build();

        return timeSlotRepository.save(slot);
    }
}