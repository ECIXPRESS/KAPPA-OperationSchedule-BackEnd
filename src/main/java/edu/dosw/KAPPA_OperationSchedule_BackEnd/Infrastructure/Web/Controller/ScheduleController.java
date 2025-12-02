package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.Controller;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.CategorySchedule;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ManageOperatingHoursUseCase manageOperatingHoursUseCase;
    private final ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;
    private final ValidateAvailabilityUseCase validateAvailabilityUseCase;
    private final GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase;
    private final ScheduleReportsUseCase scheduleReportsUseCase;
    private final ManageCategorySchedulesUseCase manageCategorySchedulesUseCase;

    public ScheduleController(
            ManageOperatingHoursUseCase manageOperatingHoursUseCase,
            ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase,
            ValidateAvailabilityUseCase validateAvailabilityUseCase,
            GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase,
            ScheduleReportsUseCase scheduleReportsUseCase,
            ManageCategorySchedulesUseCase manageCategorySchedulesUseCase) {

        this.manageOperatingHoursUseCase = manageOperatingHoursUseCase;
        this.manageTemporaryClosuresUseCase = manageTemporaryClosuresUseCase;
        this.validateAvailabilityUseCase = validateAvailabilityUseCase;
        this.getAvailableTimeSlotsUseCase = getAvailableTimeSlotsUseCase;
        this.scheduleReportsUseCase = scheduleReportsUseCase;
        this.manageCategorySchedulesUseCase = manageCategorySchedulesUseCase;
    }

// ========== CATEGORY SCHEDULES ENDPOINTS (ACTUALIZADOS) ========== //

    @PostMapping("/categories")
    public ResponseEntity<CategoryScheduleResponse> createCategorySchedule(
            @RequestBody CreateCategoryScheduleRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CreateCategoryScheduleCommand command = new CreateCategoryScheduleCommand(
                request.getCategoryName(),
                request.getStartTime(),
                request.getEndTime()
        );

        CategorySchedule result = manageCategorySchedulesUseCase.execute(command);
        CategoryScheduleResponse response = CategoryScheduleResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryScheduleResponse> updateCategorySchedule(
            @PathVariable String id,
            @RequestBody CreateCategoryScheduleRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        UpdateCategoryScheduleCommand command = new UpdateCategoryScheduleCommand(
                id,
                request.getCategoryName(),
                request.getStartTime(),
                request.getEndTime()
        );

        CategorySchedule result = manageCategorySchedulesUseCase.execute(command);
        CategoryScheduleResponse response = CategoryScheduleResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/categories/{id}/status")
    public ResponseEntity<CategoryScheduleResponse> toggleCategoryScheduleStatus(
            @PathVariable String id,
            @RequestParam Boolean active,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        ToggleCategoryStatusCommand command = new ToggleCategoryStatusCommand(id, active);

        CategorySchedule result = manageCategorySchedulesUseCase.execute(command);
        CategoryScheduleResponse response = CategoryScheduleResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    // Los endpoints de CONSULTA se mantienen igual
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryScheduleResponse>> getAllCategorySchedules(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<CategorySchedule> categories = manageCategorySchedulesUseCase.getAllCategorySchedules();
        List<CategoryScheduleResponse> response = categories.stream()
                .map(CategoryScheduleResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/active")
    public ResponseEntity<List<CategoryScheduleResponse>> getActiveCategorySchedules(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<CategorySchedule> categories = manageCategorySchedulesUseCase.getActiveCategorySchedules();
        List<CategoryScheduleResponse> response = categories.stream()
                .map(CategoryScheduleResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<CategoryScheduleResponse> getCategorySchedule(
            @PathVariable String categoryName,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CategorySchedule category = manageCategorySchedulesUseCase.getCategorySchedule(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CategoryScheduleResponse response = CategoryScheduleResponse.fromDomain(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}/active")
    public ResponseEntity<CategoryScheduleResponse> getActiveCategorySchedule(
            @PathVariable String categoryName,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CategorySchedule category = manageCategorySchedulesUseCase.getActiveCategorySchedule(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoría activa no encontrada"));

        CategoryScheduleResponse response = CategoryScheduleResponse.fromDomain(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}/status")
    public ResponseEntity<Map<String, Boolean>> isCategoryActive(
            @PathVariable String categoryName,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        boolean isActive = manageCategorySchedulesUseCase.isCategoryActive(categoryName);
        return ResponseEntity.ok(Map.of("active", isActive));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategorySchedule(
            @PathVariable String id,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);
        manageCategorySchedulesUseCase.deleteCategorySchedule(id);
        return ResponseEntity.ok().build();
    }

    // ========== AVAILABILITY ENDPOINTS ========== //

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

// ========== OPERATING HOURS ENDPOINTS (ACTUALIZADOS) ========== //

    @PostMapping("/operating-hours")
    public ResponseEntity<OperatingHoursResponse> createOperatingHours(
            @RequestBody CreateOperatingHoursRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CreateOperatingHoursCommand command = new CreateOperatingHoursCommand(
                request.getPointOfSaleId(),
                request.getDayOfWeek(),
                request.getOpeningTime(),
                request.getClosingTime()
        );

        OperatingHours result = manageOperatingHoursUseCase.execute(command);
        OperatingHoursResponse response = OperatingHoursResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/operating-hours/{id}")
    public ResponseEntity<OperatingHoursResponse> updateOperatingHours(
            @PathVariable String id,
            @RequestBody CreateOperatingHoursRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        UpdateOperatingHoursCommand command = new UpdateOperatingHoursCommand(
                id,
                request.getDayOfWeek(),
                request.getOpeningTime(),
                request.getClosingTime()
        );

        OperatingHours result = manageOperatingHoursUseCase.execute(command);
        OperatingHoursResponse response = OperatingHoursResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/operating-hours/{id}/status")
    public ResponseEntity<OperatingHoursResponse> toggleOperatingHoursStatus(
            @PathVariable String id,
            @RequestParam Boolean active,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        ToggleOperatingHoursStatusCommand command = new ToggleOperatingHoursStatusCommand(id, active);

        OperatingHours result = manageOperatingHoursUseCase.execute(command);
        OperatingHoursResponse response = OperatingHoursResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/operating-hours")
    public ResponseEntity<List<OperatingHoursResponse>> getAllOperatingHours(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<OperatingHours> operatingHours = manageOperatingHoursUseCase.getAllOperatingHours();
        List<OperatingHoursResponse> response = operatingHours.stream()
                .map(OperatingHoursResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/operating-hours/{pointOfSaleId}/active")
    public ResponseEntity<List<OperatingHoursResponse>> getActiveOperatingHoursByPointOfSale(
            @PathVariable String pointOfSaleId,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<OperatingHours> operatingHours = manageOperatingHoursUseCase.getActiveOperatingHoursByPointOfSale(pointOfSaleId);
        List<OperatingHoursResponse> response = operatingHours.stream()
                .map(OperatingHoursResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/operating-hours/active")
    public ResponseEntity<List<OperatingHoursResponse>> getAllActiveOperatingHours(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<OperatingHours> operatingHours = manageOperatingHoursUseCase.getAllActiveOperatingHours();
        List<OperatingHoursResponse> response = operatingHours.stream()
                .map(OperatingHoursResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ========== TEMPORARY CLOSURES ENDPOINTS ========== //

    @PostMapping("/temporary-closures")
    public ResponseEntity<TemporaryClosureResponse> createTemporaryClosure(
            @RequestBody CreateTemporaryClosureRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CreateTemporaryClosureCommand command = new CreateTemporaryClosureCommand(
                request.getPointOfSaleId(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getReason()
        );

        TemporaryClosure result = manageTemporaryClosuresUseCase.execute(command);
        TemporaryClosureResponse response = TemporaryClosureResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/temporary-closures/{pointOfSaleId}")
    public ResponseEntity<List<TemporaryClosureResponse>> getTemporaryClosuresByPointOfSale(
            @PathVariable String pointOfSaleId,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<TemporaryClosure> closures = manageTemporaryClosuresUseCase.getClosuresByPointOfSale(pointOfSaleId);
        List<TemporaryClosureResponse> response = closures.stream()
                .map(TemporaryClosureResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/temporary-closures")
    public ResponseEntity<List<TemporaryClosureResponse>> getAllTemporaryClosures(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<TemporaryClosure> closures = manageTemporaryClosuresUseCase.getAllTemporaryClosures();
        List<TemporaryClosureResponse> response = closures.stream()
                .map(TemporaryClosureResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/temporary-closures/active/range")
    public ResponseEntity<List<TemporaryClosureResponse>> getActiveClosuresInRange(
            @RequestParam String start,
            @RequestParam String end,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);

        List<TemporaryClosure> closures = manageTemporaryClosuresUseCase.getActiveClosuresInRange(startDateTime, endDateTime);
        List<TemporaryClosureResponse> response = closures.stream()
                .map(TemporaryClosureResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/temporary-closures/{id}")
    public ResponseEntity<TemporaryClosureResponse> updateTemporaryClosure(
            @PathVariable String id,
            @RequestBody CreateTemporaryClosureRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        UpdateTemporaryClosureCommand command = new UpdateTemporaryClosureCommand(
                id,
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getReason()
        );

        TemporaryClosure result = manageTemporaryClosuresUseCase.execute(command);
        TemporaryClosureResponse response = TemporaryClosureResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/temporary-closures/{id}/status")
    public ResponseEntity<TemporaryClosureResponse> toggleTemporaryClosureStatus(
            @PathVariable String id,
            @RequestParam Boolean active,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        ToggleTemporaryClosureStatusCommand command = new ToggleTemporaryClosureStatusCommand(id, active);

        TemporaryClosure result = manageTemporaryClosuresUseCase.execute(command);
        TemporaryClosureResponse response = TemporaryClosureResponse.fromDomain(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/temporary-closures/{id}")
    public ResponseEntity<Void> deleteTemporaryClosure(
            @PathVariable String id,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        manageTemporaryClosuresUseCase.deleteTemporaryClosure(id);
        return ResponseEntity.ok().build();
    }

    // ========== REPORTS ENDPOINTS ========== //

    @GetMapping("/reports/{pointOfSaleId}")
    public ResponseEntity<?> getPointOfSaleReport(
            @PathVariable String pointOfSaleId,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);
        var result = scheduleReportsUseCase.generatePointOfSaleReport(pointOfSaleId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/availability")
    public ResponseEntity<?> generateAvailabilityReport(
            @RequestParam String start,
            @RequestParam String end,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);
        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);

        var result = scheduleReportsUseCase.generateAvailabilityReport(startDateTime, endDateTime);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/categories")
    public ResponseEntity<?> generateCategoryScheduleReport(
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);
        var result = scheduleReportsUseCase.generateCategoryScheduleReport();
        return ResponseEntity.ok(result);
    }
}