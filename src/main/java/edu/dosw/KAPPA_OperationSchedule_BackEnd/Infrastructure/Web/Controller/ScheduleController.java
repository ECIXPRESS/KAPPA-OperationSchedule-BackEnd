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
    private final CreateTimeSlotUseCase createTimeSlotUseCase;
    private final ReserveTimeSlotUseCase reserveTimeSlotUseCase;
    private final ReleaseTimeSlotUseCase releaseTimeSlotUseCase;
    private final GenerateTimeSlotsUseCase generateTimeSlotsUseCase;

    public ScheduleController(
            ManageOperatingHoursUseCase manageOperatingHoursUseCase,
            ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase,
            ValidateAvailabilityUseCase validateAvailabilityUseCase,
            GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase,
            ScheduleReportsUseCase scheduleReportsUseCase,
            ManageCategorySchedulesUseCase manageCategorySchedulesUseCase,
            CreateTimeSlotUseCase createTimeSlotUseCase,
            ReserveTimeSlotUseCase reserveTimeSlotUseCase,
            ReleaseTimeSlotUseCase releaseTimeSlotUseCase,
            GenerateTimeSlotsUseCase generateTimeSlotsUseCase) {

        this.manageOperatingHoursUseCase = manageOperatingHoursUseCase;
        this.manageTemporaryClosuresUseCase = manageTemporaryClosuresUseCase;
        this.validateAvailabilityUseCase = validateAvailabilityUseCase;
        this.getAvailableTimeSlotsUseCase = getAvailableTimeSlotsUseCase;
        this.scheduleReportsUseCase = scheduleReportsUseCase;
        this.manageCategorySchedulesUseCase = manageCategorySchedulesUseCase;
        this.createTimeSlotUseCase = createTimeSlotUseCase;
        this.reserveTimeSlotUseCase = reserveTimeSlotUseCase;
        this.releaseTimeSlotUseCase = releaseTimeSlotUseCase;
        this.generateTimeSlotsUseCase = generateTimeSlotsUseCase;
    }


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


    @PostMapping("/time-slots")
    public ResponseEntity<SuccessResponse<TimeSlotResponse>> createTimeSlot(
            @RequestBody CreateTimeSlotRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        CreateTimeSlotCommand command = new CreateTimeSlotCommand(
                request.getPointOfSaleId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getAvailableCapacity()
        );

        command.validate();
        TimeSlot result = createTimeSlotUseCase.execute(command);
        TimeSlotResponse response = TimeSlotResponse.fromDomain(result);

        return ResponseEntity.ok(SuccessResponse.create("TimeSlot creado exitosamente", response));
    }

    @PostMapping("/time-slots/generate")
    public ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> generateTimeSlots(
            @RequestBody GenerateTimeSlotsRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        GenerateTimeSlotsCommand command = new GenerateTimeSlotsCommand(
                request.getPointOfSaleId(),
                request.getDate(),
                request.getSlotDurationMinutes(),
                request.getDefaultCapacity()
        );

        command.validate();
        List<TimeSlot> result = generateTimeSlotsUseCase.execute(command);
        List<TimeSlotResponse> response = result.stream()
                .map(TimeSlotResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(SuccessResponse.create(
                String.format("Se generaron %d slots exitosamente", result.size()),
                response
        ));
    }

    @PostMapping("/time-slots/{slotId}/reserve")
    public ResponseEntity<SuccessResponse<TimeSlotResponse>> reserveTimeSlot(
            @PathVariable String slotId,
            @RequestBody ReserveTimeSlotRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        ReserveTimeSlotCommand command = new ReserveTimeSlotCommand(
                slotId,
                request.getOrderId(),
                request.getUserId()
        );

        command.validate();
        TimeSlot result = reserveTimeSlotUseCase.execute(command);
        TimeSlotResponse response = TimeSlotResponse.fromDomain(result);

        return ResponseEntity.ok(SuccessResponse.create("Slot reservado exitosamente", response));
    }

    @PostMapping("/time-slots/{slotId}/release")
    public ResponseEntity<SuccessResponse<TimeSlotResponse>> releaseTimeSlot(
            @PathVariable String slotId,
            @RequestBody ReleaseTimeSlotRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        ReleaseTimeSlotCommand command = new ReleaseTimeSlotCommand(
                slotId,
                request.getOrderId()
        );

        command.validate();
        TimeSlot result = releaseTimeSlotUseCase.execute(command);
        TimeSlotResponse response = TimeSlotResponse.fromDomain(result);

        return ResponseEntity.ok(SuccessResponse.create("Slot liberado exitosamente", response));
    }


    @GetMapping("/time-slots/available")
    public ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> getAvailableTimeSlotsWithFilters(
            @RequestBody GetAvailableTimeSlotsRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<TimeSlot> timeSlots;

        if (request.getMinCapacity() != null) {
            timeSlots = getAvailableTimeSlotsUseCase.getAvailableTimeSlotsWithMinCapacity(
                    request.getPointOfSaleId(),
                    request.getDate(),
                    request.getMinCapacity()
            );
        } else if (request.getProductCategory() != null && !request.getProductCategory().isEmpty()) {
            timeSlots = getAvailableTimeSlotsUseCase.getAvailableTimeSlotsByProductCategory(
                    request.getPointOfSaleId(),
                    request.getDate(),
                    request.getProductCategory()
            );
        } else {
            timeSlots = getAvailableTimeSlotsUseCase.getAvailableTimeSlots(
                    request.getPointOfSaleId(),
                    request.getDate()
            );
        }

        List<TimeSlotResponse> response = timeSlots.stream()
                .map(TimeSlotResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(SuccessResponse.create(
                String.format("Se encontraron %d slots disponibles", response.size()),
                response
        ));
    }

    @GetMapping("/time-slots/{pointOfSaleId}/now")
    public ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> getAvailableTimeSlotsForNow(
            @PathVariable String pointOfSaleId,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        List<TimeSlot> timeSlots = getAvailableTimeSlotsUseCase.getAvailableTimeSlotsForNow(pointOfSaleId);
        List<TimeSlotResponse> response = timeSlots.stream()
                .map(TimeSlotResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(SuccessResponse.create(
                String.format("Slots disponibles en las próximas 2 horas: %d", response.size()),
                response
        ));
    }

    @GetMapping("/time-slots/{pointOfSaleId}/closures-validation")
    public ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> getAvailableTimeSlotsWithClosuresValidation(
            @PathVariable String pointOfSaleId,
            @RequestParam String date,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        LocalDate localDate = LocalDate.parse(date);
        List<TimeSlot> timeSlots = getAvailableTimeSlotsUseCase
                .getAvailableTimeSlotsWithClosuresValidation(pointOfSaleId, localDate);

        List<TimeSlotResponse> response = timeSlots.stream()
                .map(TimeSlotResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(SuccessResponse.create(
                String.format("Slots disponibles después de validar cierres: %d", response.size()),
                response
        ));
    }


    @GetMapping("/reports/time-slots/occupancy")
    public ResponseEntity<SuccessResponse<ScheduleReportResponse>> generateTimeSlotOccupancyReport(
            @RequestParam String pointOfSaleId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, Object> reportData = scheduleReportsUseCase
                .generateTimeSlotOccupancyReport(pointOfSaleId, start, end);

        ScheduleReportResponse response = ScheduleReportResponse.create(
                "time_slot_occupancy_report",
                reportData
        );

        return ResponseEntity.ok(SuccessResponse.create("Reporte de ocupación generado", response));
    }

    @GetMapping("/reports/time-slots/peak-hours")
    public ResponseEntity<SuccessResponse<ScheduleReportResponse>> generatePeakHoursReport(
            @RequestParam String pointOfSaleId,
            @RequestParam String date,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        LocalDate reportDate = LocalDate.parse(date);
        Map<String, Object> reportData = scheduleReportsUseCase
                .generatePeakHoursReport(pointOfSaleId, reportDate);

        ScheduleReportResponse response = ScheduleReportResponse.create(
                "peak_hours_report",
                reportData
        );

        return ResponseEntity.ok(SuccessResponse.create("Reporte de horas pico generado", response));
    }

    @GetMapping("/reports/time-slots/capacity-utilization")
    public ResponseEntity<SuccessResponse<ScheduleReportResponse>> generateCapacityUtilizationReport(
            @RequestParam String pointOfSaleId,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        Map<String, Object> reportData = scheduleReportsUseCase
                .generateCapacityUtilizationReport(pointOfSaleId);

        ScheduleReportResponse response = ScheduleReportResponse.create(
                "capacity_utilization_report",
                reportData
        );

        return ResponseEntity.ok(SuccessResponse.create("Reporte de utilización de capacidad generado", response));
    }


    @PostMapping("/availability/with-suggestions")
    public ResponseEntity<SuccessResponse<AvailabilityResponse>> checkAvailabilityWithSuggestions(
            @RequestBody AvailabilityCheckRequest request,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailabilityWithSuggestions(
                request.getPointOfSaleId(),
                request.getRequestedTime(),
                request.getProductCategory()
        );

        AvailabilityResponse response = AvailabilityResponse.fromDomain(result);
        return ResponseEntity.ok(SuccessResponse.create(
                result.getAvailable() ? "Disponible" : "No disponible - ver sugerencias",
                response
        ));
    }

    @PostMapping("/availability/order")
    public ResponseEntity<SuccessResponse<AvailabilityResponse>> validateOrderAvailability(
            @RequestParam String pointOfSaleId,
            @RequestParam String requestedTime,
            @RequestBody List<String> productCategories,
            @AuthenticationPrincipal String username) {

        System.out.println("Usuario autenticado: " + username);

        LocalDateTime dateTime = LocalDateTime.parse(requestedTime);
        AvailabilityResult result = validateAvailabilityUseCase.validateOrderAvailability(
                pointOfSaleId,
                dateTime,
                productCategories
        );

        AvailabilityResponse response = AvailabilityResponse.fromDomain(result);
        return ResponseEntity.ok(SuccessResponse.create(
                result.getAvailable() ? "Todos los productos disponibles" : "Algunos productos no disponibles",
                response
        ));
    }
}