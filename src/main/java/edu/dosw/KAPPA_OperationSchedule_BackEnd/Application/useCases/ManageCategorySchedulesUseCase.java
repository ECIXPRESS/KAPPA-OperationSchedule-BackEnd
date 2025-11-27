package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
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

    public CategorySchedule createCategorySchedule(String categoryName, LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw BusinessException.validationError("La hora de inicio no puede ser después de la hora de fin");
        }

        if (categoryScheduleRepository.findByCategoryName(categoryName).isPresent()) {
            throw BusinessException.validationError("Ya existe una categoría con el nombre: " + categoryName);
        }

        CategorySchedule categorySchedule = new CategorySchedule(categoryName, startTime, endTime);
        return categoryScheduleRepository.save(categorySchedule);
    }

    public Optional<CategorySchedule> getCategorySchedule(String categoryName) {
        Optional<CategorySchedule> schedule = categoryScheduleRepository.findByCategoryName(categoryName);
        if (schedule.isEmpty()) {
            throw BusinessException.validationError("No se encontró la categoría: " + categoryName);
        }
        return schedule;
    }

    public List<CategorySchedule> getAllCategorySchedules() {
        List<CategorySchedule> schedules = categoryScheduleRepository.findAll();
        if (schedules.isEmpty()) {
            throw BusinessException.validationError("No hay categorías configuradas");
        }
        return schedules;
    }
}