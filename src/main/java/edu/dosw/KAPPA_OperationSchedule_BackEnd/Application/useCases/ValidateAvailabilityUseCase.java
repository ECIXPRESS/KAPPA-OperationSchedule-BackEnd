package edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ValidateAvailabilityUseCase {

    private final OperatingHoursRepositoryPort operatingHoursRepository;
    private final TemporaryClosureRepositoryPort temporaryClosureRepository;
    private final CategoryScheduleRepositoryPort categoryScheduleRepository;

    public ValidateAvailabilityUseCase(OperatingHoursRepositoryPort operatingHoursRepository, TemporaryClosureRepositoryPort temporaryClosureRepository, CategoryScheduleRepositoryPort categoryScheduleRepository) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.temporaryClosureRepository = temporaryClosureRepository;
        this.categoryScheduleRepository = categoryScheduleRepository;
    }

    public AvailabilityResult validatePointOfSaleAvailability(String pointOfSaleId, LocalDateTime requestedTime) {
        List<TemporaryClosure> activeClosures = temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime);

        if (!activeClosures.isEmpty()) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "Punto de venta cerrado temporalmente");
        }

        DayOfWeek dayOfWeek = requestedTime.getDayOfWeek();
        LocalTime requestedLocalTime = requestedTime.toLocalTime();
        List<OperatingHours> operatingHours = operatingHoursRepository
                .findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek);

        boolean isWithinOperatingHours = false;
        for (OperatingHours oh : operatingHours) {
            if (oh.getActive() && !requestedLocalTime.isBefore(oh.getOpeningTime()) && !requestedLocalTime.isAfter(oh.getClosingTime())) {
                isWithinOperatingHours = true;
                break;
            }
        }

        if (!isWithinOperatingHours) {
            return new AvailabilityResult(false, pointOfSaleId, requestedTime,
                    "Fuera del horario de atención");
        }

        return new AvailabilityResult(true, pointOfSaleId, requestedTime, "Disponible");
    }

    public AvailabilityResult validateProductCategoryAvailability(String pointOfSaleId, LocalDateTime requestedTime, String productCategory) {

        AvailabilityResult pointOfSaleAvailability = validatePointOfSaleAvailability(pointOfSaleId, requestedTime);
        if (!pointOfSaleAvailability.getAvailable()) {
            return pointOfSaleAvailability;
        }

        if (productCategory != null && !productCategory.isEmpty()) {
            Optional<CategorySchedule> categorySchedule = categoryScheduleRepository.findByCategoryName(productCategory);

            if (categorySchedule.isPresent()) {
                CategorySchedule schedule = categorySchedule.get();
                LocalTime requestedLocalTime = requestedTime.toLocalTime();
                LocalTime startTime = schedule.getStartTime();
                LocalTime endTime = schedule.getEndTime();

                if (requestedLocalTime.isBefore(startTime) || requestedLocalTime.isAfter(endTime)) {
                    AvailabilityResult result = new AvailabilityResult(false, pointOfSaleId, requestedTime, "Producto fuera de horario");
                    return result;
                }
            }
        }

        return new AvailabilityResult(true, pointOfSaleId, requestedTime, "Disponible");
    }
}