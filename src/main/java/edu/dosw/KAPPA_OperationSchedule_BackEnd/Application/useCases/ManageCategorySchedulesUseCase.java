package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
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
        CategorySchedule categorySchedule = new CategorySchedule(categoryName, startTime, endTime);
        return categoryScheduleRepository.save(categorySchedule);
    }

    public Optional<CategorySchedule> getCategorySchedule(String categoryName) {
        return categoryScheduleRepository.findByCategoryName(categoryName);
    }

    public List<CategorySchedule> getAllCategorySchedules() {
        return categoryScheduleRepository.findAll();
    }
}