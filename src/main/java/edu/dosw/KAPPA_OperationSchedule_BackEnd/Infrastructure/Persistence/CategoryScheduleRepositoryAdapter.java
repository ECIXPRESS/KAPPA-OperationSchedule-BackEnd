package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryScheduleRepositoryAdapter implements CategoryScheduleRepositoryPort {

    private final CategoryScheduleRepository categoryScheduleRepository;

    public CategoryScheduleRepositoryAdapter(CategoryScheduleRepository categoryScheduleRepository) {
        this.categoryScheduleRepository = categoryScheduleRepository;
    }

    @Override
    public CategorySchedule save(CategorySchedule categorySchedule) {
        return categoryScheduleRepository.save(categorySchedule);
    }

    @Override
    public Optional<CategorySchedule> findById(String id) {
        return categoryScheduleRepository.findById(id);
    }

    @Override
    public Optional<CategorySchedule> findByCategoryName(String categoryName) {
        return categoryScheduleRepository.findByCategoryName(categoryName);
    }

    @Override
    public Optional<CategorySchedule> findActiveByCategoryName(String categoryName) {
        return categoryScheduleRepository.findByCategoryName(categoryName)
                .filter(CategorySchedule::getActive);
    }

    @Override
    public List<CategorySchedule> findAllActive() {
        return categoryScheduleRepository.findByActiveTrue();
    }

    @Override
    public void deleteById(String id) {
        categoryScheduleRepository.deleteById(id);
    }

    @Override
    public List<CategorySchedule> findAll() {
        return categoryScheduleRepository.findAll();
    }
}