package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.Controller;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ManageOperatingHoursUseCase manageOperatingHoursUseCase;
    private final ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;
    private final ValidateAvailabilityUseCase validateAvailabilityUseCase;
    private final GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase;
    private final ScheduleReportsUseCase scheduleReportsUseCase;

    public ScheduleController(
            ManageOperatingHoursUseCase manageOperatingHoursUseCase,
            ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase,
            ValidateAvailabilityUseCase validateAvailabilityUseCase,
            GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase,
            ScheduleReportsUseCase scheduleReportsUseCase) {

        this.manageOperatingHoursUseCase = manageOperatingHoursUseCase;
        this.manageTemporaryClosuresUseCase = manageTemporaryClosuresUseCase;
        this.validateAvailabilityUseCase = validateAvailabilityUseCase;
        this.getAvailableTimeSlotsUseCase = getAvailableTimeSlotsUseCase;
        this.scheduleReportsUseCase = scheduleReportsUseCase;
    }

    @PostMapping("/operating-hours")
    public ResponseEntity<OperatingHoursResponse> createOperatingHours(
            @RequestBody CreateOperatingHoursRequest request) {

        OperatingHours result = manageOperatingHoursUseCase.createOperatingHours(
                request.getPointOfSaleId(),
                request.getDayOfWeek(),
                request.getOpeningTime(),
                request.getClosingTime()
        );

        OperatingHoursResponse response = OperatingHoursResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/operating-hours/{pointOfSaleId}")
    public ResponseEntity<List<OperatingHoursResponse>> getOperatingHoursByPointOfSale(
            @PathVariable String pointOfSaleId) {

        List<OperatingHours> operatingHours = manageOperatingHoursUseCase.getOperatingHoursByPointOfSale(pointOfSaleId);
        List<OperatingHoursResponse> response = operatingHours.stream()
                .map(OperatingHoursResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/operating-hours/{pointOfSaleId}/{dayOfWeek}")
    public ResponseEntity<List<OperatingHoursResponse>> getOperatingHoursByPointOfSaleAndDay(
            @PathVariable String pointOfSaleId,
            @PathVariable String dayOfWeek) {

        java.time.DayOfWeek day = java.time.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        List<OperatingHours> operatingHours = manageOperatingHoursUseCase.getOperatingHoursByPointOfSaleAndDay(pointOfSaleId, day);
        List<OperatingHoursResponse> response = operatingHours.stream()
                .map(OperatingHoursResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/operating-hours/{id}")
    public ResponseEntity<Void> deleteOperatingHours(@PathVariable String id) {
        manageOperatingHoursUseCase.deleteOperatingHours(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/temporary-closures")
    public ResponseEntity<TemporaryClosureResponse> createTemporaryClosure(
            @RequestBody CreateTemporaryClosureRequest request) {

        TemporaryClosure result = manageTemporaryClosuresUseCase.createTemporaryClosure(
                request.getPointOfSaleId(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getReason()
        );

        TemporaryClosureResponse response = TemporaryClosureResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/temporary-closures/{pointOfSaleId}")
    public ResponseEntity<List<TemporaryClosureResponse>> getTemporaryClosuresByPointOfSale(
            @PathVariable String pointOfSaleId) {

        List<TemporaryClosure> closures = manageTemporaryClosuresUseCase.getClosuresByPointOfSale(pointOfSaleId);
        List<TemporaryClosureResponse> response = closures.stream()
                .map(TemporaryClosureResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/availability")
    public ResponseEntity<AvailabilityResponse> checkAvailability(
            @RequestBody AvailabilityCheckRequest request) {

        AvailabilityResult result = validateAvailabilityUseCase.validateProductCategoryAvailability(
                request.getPointOfSaleId(),
                request.getRequestedTime(),
                request.getProductCategory()
        );

        AvailabilityResponse response = AvailabilityResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time-slots/{pointOfSaleId}")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable String pointOfSaleId,
            @RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);
        List<TimeSlot> timeSlots = getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointOfSaleId, localDate);
        List<TimeSlotResponse> response = timeSlots.stream()
                .map(TimeSlotResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/{pointOfSaleId}")
    public ResponseEntity<?> getPointOfSaleReport(@PathVariable String pointOfSaleId) {
        var result = scheduleReportsUseCase.generatePointOfSaleReport(pointOfSaleId);
        return ResponseEntity.ok(result);
    }
}