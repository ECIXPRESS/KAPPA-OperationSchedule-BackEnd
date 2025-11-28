package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.CreateCategoryScheduleCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.UpdateCategoryScheduleCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.ToggleCategoryStatusCommand;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ManageCategorySchedulesUseCase {

    private final CategoryScheduleRepositoryPort categoryScheduleRepository;

    public ManageCategorySchedulesUseCase(CategoryScheduleRepositoryPort categoryScheduleRepository) {
        this.categoryScheduleRepository = categoryScheduleRepository;
    }

    // ========== COMMAND METHODS ========== //

    /**
     * Crea una nueva categoría de horario
     */
    public CategorySchedule execute(CreateCategoryScheduleCommand command) {
        // Validación de negocio: nombre único
        if (categoryScheduleRepository.findByCategoryName(command.getCategoryName()).isPresent()) {
            throw BusinessException.validationError("Ya existe una categoría con el nombre: " + command.getCategoryName());
        }

        CategorySchedule categorySchedule = new CategorySchedule(
                command.getCategoryName(),
                command.getStartTime(),
                command.getEndTime()
        );
        return categoryScheduleRepository.save(categorySchedule);
    }

    /**
     * Actualiza una categoría existente
     */
    public CategorySchedule execute(UpdateCategoryScheduleCommand command) {
        CategorySchedule existing = categoryScheduleRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.validationError("Categoría no encontrada con ID: " + command.getId()));

        // Validar conflicto de nombre solo si cambió
        if (!existing.getCategoryName().equals(command.getCategoryName())) {
            categoryScheduleRepository.findByCategoryName(command.getCategoryName())
                    .ifPresent(conflict -> {
                        throw BusinessException.validationError("Ya existe una categoría con el nombre: " + command.getCategoryName());
                    });
        }

        existing.setCategoryName(command.getCategoryName());
        existing.setStartTime(command.getStartTime());
        existing.setEndTime(command.getEndTime());

        return categoryScheduleRepository.save(existing);
    }

    /**
     * Activa/desactiva una categoría
     */
    public CategorySchedule execute(ToggleCategoryStatusCommand command) {
        CategorySchedule category = categoryScheduleRepository.findById(command.getId())
                .orElseThrow(() -> BusinessException.validationError("Categoría no encontrada con ID: " + command.getId()));

        category.setActive(command.getActive());
        return categoryScheduleRepository.save(category);
    }

    // ========== QUERY METHODS ========== //

    /**
     * Obtiene una categoría por nombre (puede estar inactiva)
     */
    public Optional<CategorySchedule> getCategorySchedule(String categoryName) {
        Optional<CategorySchedule> schedule = categoryScheduleRepository.findByCategoryName(categoryName);
        if (schedule.isEmpty()) {
            throw BusinessException.validationError("No se encontró la categoría: " + categoryName);
        }
        return schedule;
    }

    /**
     * Obtiene todas las categorías (activas e inactivas)
     */
    public List<CategorySchedule> getAllCategorySchedules() {
        List<CategorySchedule> schedules = categoryScheduleRepository.findAll();
        if (schedules.isEmpty()) {
            throw BusinessException.validationError("No hay categorías configuradas");
        }
        return schedules;
    }

    /**
     * Obtiene solo las categorías activas
     */
    public List<CategorySchedule> getActiveCategorySchedules() {
        List<CategorySchedule> activeSchedules = categoryScheduleRepository.findAllActive();
        if (activeSchedules.isEmpty()) {
            throw BusinessException.validationError("No hay categorías activas configuradas");
        }
        return activeSchedules;
    }

    /**
     * Obtiene una categoría activa por nombre
     */
    public Optional<CategorySchedule> getActiveCategorySchedule(String categoryName) {
        Optional<CategorySchedule> schedule = categoryScheduleRepository.findActiveByCategoryName(categoryName);
        if (schedule.isEmpty()) {
            throw BusinessException.validationError("No se encontró la categoría activa: " + categoryName);
        }
        return schedule;
    }

    /**
     * Verifica si una categoría está activa
     */
    public boolean isCategoryActive(String categoryName) {
        return categoryScheduleRepository.findActiveByCategoryName(categoryName).isPresent();
    }

    /**
     * Elimina una categoría por ID
     */
    public void deleteCategorySchedule(String id) {
        boolean exists = categoryScheduleRepository.findById(id).isPresent();
        if (!exists) {
            throw BusinessException.validationError("Categoría no encontrada con ID: " + id);
        }
        categoryScheduleRepository.deleteById(id);
    }

    // ========== MÉTODOS DE CONVERSIÓN (para backward compatibility) ========== //

    /**
     * Método legacy - mantener por compatibilidad temporal
     * @deprecated Usar execute(CreateCategoryScheduleCommand command) en su lugar
     */
    @Deprecated
    public CategorySchedule createCategorySchedule(String categoryName, LocalTime startTime, LocalTime endTime) {
        CreateCategoryScheduleCommand command = new CreateCategoryScheduleCommand(categoryName, startTime, endTime);
        return execute(command);
    }

    /**
     * Método legacy - mantener por compatibilidad temporal
     * @deprecated Usar execute(UpdateCategoryScheduleCommand command) en su lugar
     */
    @Deprecated
    public CategorySchedule updateCategorySchedule(String id, String categoryName, LocalTime startTime, LocalTime endTime) {
        UpdateCategoryScheduleCommand command = new UpdateCategoryScheduleCommand(id, categoryName, startTime, endTime);
        return execute(command);
    }

    /**
     * Método legacy - mantener por compatibilidad temporal
     * @deprecated Usar execute(ToggleCategoryStatusCommand command) en su lugar
     */
    @Deprecated
    public CategorySchedule toggleCategoryScheduleStatus(String id, Boolean active) {
        ToggleCategoryStatusCommand command = new ToggleCategoryStatusCommand(id, active);
        return execute(command);
    }
}