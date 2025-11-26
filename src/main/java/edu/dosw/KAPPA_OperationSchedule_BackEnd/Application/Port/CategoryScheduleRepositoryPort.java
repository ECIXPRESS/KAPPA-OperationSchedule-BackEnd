package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import java.util.List;
import java.util.Optional;

public interface CategoryScheduleRepositoryPort {
    CategorySchedule save(CategorySchedule categorySchedule);
    Optional<CategorySchedule> findById(String id);
    Optional<CategorySchedule> findByCategoryName(String categoryName);
    Optional<CategorySchedule> findActiveByCategoryName(String categoryName);
    List<CategorySchedule> findAllActive();
    void deleteById(String id);
    List<CategorySchedule> findAll();
}