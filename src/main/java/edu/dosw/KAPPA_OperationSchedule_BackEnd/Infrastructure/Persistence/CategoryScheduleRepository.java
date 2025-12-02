package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Persistence;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryScheduleRepository extends MongoRepository<CategorySchedule, String> {

    Optional<CategorySchedule> findByCategoryName(String categoryName);
    List<CategorySchedule> findByActiveTrue();

    default Optional<CategorySchedule> findActiveByCategoryName(String categoryName) {
        Optional<CategorySchedule> categorySchedule = findByCategoryName(categoryName);
        return categorySchedule.filter(schedule -> schedule.getActive() != null && schedule.getActive());
    }

    default List<CategorySchedule> findAllActive() {
        return findByActiveTrue();
    }

    Optional<CategorySchedule> findById(String id);

    void deleteById(String id);
}