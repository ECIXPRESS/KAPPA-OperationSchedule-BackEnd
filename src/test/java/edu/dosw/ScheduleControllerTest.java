package edu.dosw;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.Controller.ScheduleController;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock private ManageOperatingHoursUseCase manageOperatingHoursUseCase;
    @Mock private ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;
    @Mock private ValidateAvailabilityUseCase validateAvailabilityUseCase;
    @Mock private GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase;
    @Mock private ScheduleReportsUseCase scheduleReportsUseCase;
    @Mock private ManageCategorySchedulesUseCase manageCategorySchedulesUseCase;
    @Mock private CreateTimeSlotUseCase createTimeSlotUseCase;
    @Mock private ReserveTimeSlotUseCase reserveTimeSlotUseCase;
    @Mock private ReleaseTimeSlotUseCase releaseTimeSlotUseCase;
    @Mock private GenerateTimeSlotsUseCase generateTimeSlotsUseCase;

    @InjectMocks private ScheduleController scheduleController;

    private CategorySchedule testCategorySchedule;
    private OperatingHours testOperatingHours;
    private TemporaryClosure testTemporaryClosure;
    private TimeSlot testTimeSlot;
    private AvailabilityResult testAvailabilityResult;

    @BeforeEach
    void setUp() {
        testCategorySchedule = createCategorySchedule("test-cat-id", "Desayuno",
                LocalTime.of(7, 0), LocalTime.of(11, 30), true);

        testOperatingHours = createOperatingHours("test-op-id", "point-001",
                DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), true);

        testTemporaryClosure = createTemporaryClosure("test-closure-id", "point-001",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                "Mantenimiento", true);

        testTimeSlot = createTimeSlot("test-slot-id", "point-001",
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
                10, 0, true);

        testAvailabilityResult = new AvailabilityResult(true, "point-001",
                LocalDateTime.now().plusHours(2), "Disponible");
        testAvailabilityResult.setCategoryMessage("");
        testAvailabilityResult.setAvailableTimeSlots(new ArrayList<>());
    }

    private CategorySchedule createCategorySchedule(String id, String name,
                                                    LocalTime start, LocalTime end, boolean active) {
        CategorySchedule cs = new CategorySchedule(name, start, end);
        cs.setId(id);
        cs.setActive(active);
        return cs;
    }

    private OperatingHours createOperatingHours(String id, String pointId,
                                                DayOfWeek day, LocalTime open, LocalTime close, boolean active) {
        OperatingHours oh = new OperatingHours(pointId, day, open, close);
        oh.setId(id);
        oh.setActive(active);
        return oh;
    }

    private TemporaryClosure createTemporaryClosure(String id, String pointId,
                                                    LocalDateTime start, LocalDateTime end, String reason, boolean active) {
        TemporaryClosure tc = new TemporaryClosure(pointId, start, end, reason);
        tc.setId(id);
        tc.setActive(active);
        return tc;
    }

    private TimeSlot createTimeSlot(String id, String pointId, LocalDateTime start,
                                    LocalDateTime end, int capacity, int booked, boolean available) {
        return TimeSlot.builder()
                .id(id).pointOfSaleId(pointId)
                .startTime(start).endTime(end)
                .availableCapacity(capacity).bookedCount(booked)
                .available(available).build();
    }

    @Test
    void createCategorySchedule_Success() {
        CreateCategoryScheduleRequest request = new CreateCategoryScheduleRequest(
                "Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30));

        when(manageCategorySchedulesUseCase.execute(any(CreateCategoryScheduleCommand.class)))
                .thenReturn(testCategorySchedule);

        ResponseEntity<CategoryScheduleResponse> response = scheduleController.createCategorySchedule(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test-cat-id", response.getBody().getId());
    }

    @Test
    void updateCategorySchedule_Success() {
        String id = "test-cat-id";
        CreateCategoryScheduleRequest request = new CreateCategoryScheduleRequest(
                "Desayuno Actualizado", LocalTime.of(7, 30), LocalTime.of(12, 0));

        CategorySchedule updated = createCategorySchedule(id, "Desayuno Actualizado",
                LocalTime.of(7, 30), LocalTime.of(12, 0), true);

        when(manageCategorySchedulesUseCase.execute(any(UpdateCategoryScheduleCommand.class)))
                .thenReturn(updated);

        ResponseEntity<CategoryScheduleResponse> response = scheduleController.updateCategorySchedule(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Desayuno Actualizado", response.getBody().getCategoryName());
    }

    @ParameterizedTest
    @CsvSource({"true,false", "false,true"})
    void toggleCategoryScheduleStatus_Success(boolean initial, boolean expected) {
        String id = "test-cat-id";
        CategorySchedule category = createCategorySchedule(id, "Desayuno",
                LocalTime.of(7, 0), LocalTime.of(11, 30), expected);

        when(manageCategorySchedulesUseCase.execute(any(ToggleCategoryStatusCommand.class)))
                .thenReturn(category);

        ResponseEntity<CategoryScheduleResponse> response = scheduleController.toggleCategoryScheduleStatus(id, expected);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody().getActive());
    }

    @Test
    void getAllCategorySchedules_Success() {
        List<CategorySchedule> categories = Arrays.asList(
                createCategorySchedule("cat1", "Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30), true),
                createCategorySchedule("cat2", "Almuerzo", LocalTime.of(11, 30), LocalTime.of(16, 0), true)
        );

        when(manageCategorySchedulesUseCase.getAllCategorySchedules()).thenReturn(categories);

        ResponseEntity<List<CategoryScheduleResponse>> response = scheduleController.getAllCategorySchedules();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getActiveCategorySchedules_Success() {
        List<CategorySchedule> categories = Arrays.asList(testCategorySchedule);
        when(manageCategorySchedulesUseCase.getActiveCategorySchedules()).thenReturn(categories);

        ResponseEntity<List<CategoryScheduleResponse>> response = scheduleController.getActiveCategorySchedules();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCategorySchedule_Success() {
        when(manageCategorySchedulesUseCase.getCategorySchedule(any())).thenReturn(Optional.of(testCategorySchedule));

        ResponseEntity<CategoryScheduleResponse> response = scheduleController.getCategorySchedule("Desayuno");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-cat-id", response.getBody().getId());
    }

    @Test
    void getCategorySchedule_NotFound() {
        when(manageCategorySchedulesUseCase.getCategorySchedule(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> scheduleController.getCategorySchedule("Cena"));
    }

    @Test
    void getActiveCategorySchedule_Success() {
        when(manageCategorySchedulesUseCase.getActiveCategorySchedule(any())).thenReturn(Optional.of(testCategorySchedule));

        ResponseEntity<CategoryScheduleResponse> response = scheduleController.getActiveCategorySchedule("Desayuno");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getActiveCategorySchedule_NotFound() {
        when(manageCategorySchedulesUseCase.getActiveCategorySchedule(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> scheduleController.getActiveCategorySchedule("Cena"));
    }

    @Test
    void isCategoryActive_Success() {
        when(manageCategorySchedulesUseCase.isCategoryActive(any())).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> response = scheduleController.isCategoryActive("Desayuno");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("active"));
    }

    @Test
    void deleteCategorySchedule_Success() {
        doNothing().when(manageCategorySchedulesUseCase).deleteCategorySchedule(any());

        ResponseEntity<Void> response = scheduleController.deleteCategorySchedule("test-cat-id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(manageCategorySchedulesUseCase, times(1)).deleteCategorySchedule("test-cat-id");
    }

    @Test
    void checkAvailability_Success() {
        AvailabilityCheckRequest request = new AvailabilityCheckRequest();
        request.setPointOfSaleId("point-001");
        request.setRequestedTime(LocalDateTime.now().plusHours(2));
        request.setProductCategory("Desayuno");

        when(validateAvailabilityUseCase.validateProductCategoryAvailability(any(), any(), any()))
                .thenReturn(testAvailabilityResult);

        ResponseEntity<AvailabilityResponse> response = scheduleController.checkAvailability(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getAvailable());
    }

    @Test
    void checkAvailabilityWithSuggestions_Success() {
        AvailabilityCheckRequest request = new AvailabilityCheckRequest();
        request.setPointOfSaleId("point-001");
        request.setRequestedTime(LocalDateTime.now().plusHours(2));
        request.setProductCategory("Desayuno");

        testAvailabilityResult.setAvailable(false);
        testAvailabilityResult.setAvailableTimeSlots(Arrays.asList("10:00", "11:00"));

        when(validateAvailabilityUseCase.validateAvailabilityWithSuggestions(any(), any(), any()))
                .thenReturn(testAvailabilityResult);

        ResponseEntity<SuccessResponse<AvailabilityResponse>> response =
                scheduleController.checkAvailabilityWithSuggestions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getData().getAvailable());
        assertEquals(2, response.getBody().getData().getAvailableTimeSlots().size());
    }

    @Test
    void validateOrderAvailability_Success() {
        OrderAvailabilityRequest request = new OrderAvailabilityRequest();
        request.setPointOfSaleId("point-001");
        request.setRequestedTime(LocalDateTime.now().plusHours(2));
        request.setProductCategories(Arrays.asList("Desayuno", "Bebidas"));

        when(validateAvailabilityUseCase.validateOrderAvailability(any(), any(), any()))
                .thenReturn(testAvailabilityResult);

        ResponseEntity<SuccessResponse<AvailabilityResponse>> response =
                scheduleController.validateOrderAvailability(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().getAvailable());
    }

    @Test
    void createTimeSlot_Success() {
        CreateTimeSlotRequest request = new CreateTimeSlotRequest(
                "point-001", LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 10);

        when(createTimeSlotUseCase.execute(any(CreateTimeSlotCommand.class)))
                .thenReturn(testTimeSlot);

        ResponseEntity<SuccessResponse<TimeSlotResponse>> response = scheduleController.createTimeSlot(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-slot-id", response.getBody().getData().getId());
    }

    @Test
    void generateTimeSlots_Success() {
        GenerateTimeSlotsRequest request = new GenerateTimeSlotsRequest(
                "point-001", LocalDate.now(), 30, 10);

        when(generateTimeSlotsUseCase.execute(any(GenerateTimeSlotsCommand.class)))
                .thenReturn(List.of(testTimeSlot));

        ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> response = scheduleController.generateTimeSlots(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void reserveTimeSlot_Success() {
        String slotId = "test-slot-id";
        ReserveTimeSlotRequest request = new ReserveTimeSlotRequest(slotId, "order-001", "user-001");

        TimeSlot reserved = createTimeSlot(slotId, "point-001",
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 10, 1, true);

        when(reserveTimeSlotUseCase.execute(any(ReserveTimeSlotCommand.class)))
                .thenReturn(reserved);

        ResponseEntity<SuccessResponse<TimeSlotResponse>> response = scheduleController.reserveTimeSlot(slotId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().getBookedCount());
    }

    @Test
    void releaseTimeSlot_Success() {
        String slotId = "test-slot-id";
        ReleaseTimeSlotRequest request = new ReleaseTimeSlotRequest(slotId, "order-001");

        TimeSlot released = createTimeSlot(slotId, "point-001",
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 10, 0, true);

        when(releaseTimeSlotUseCase.execute(any(ReleaseTimeSlotCommand.class)))
                .thenReturn(released);

        ResponseEntity<SuccessResponse<TimeSlotResponse>> response = scheduleController.releaseTimeSlot(slotId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getData().getBookedCount());
    }

    @Test
    void getAvailableTimeSlots_Success() {
        when(getAvailableTimeSlotsUseCase.getAvailableTimeSlots(any(), any()))
                .thenReturn(List.of(testTimeSlot));

        ResponseEntity<List<TimeSlotResponse>> response = scheduleController.getAvailableTimeSlots("point-001", "2024-01-15");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @ParameterizedTest
    @MethodSource("filterParameters")
    void getAvailableTimeSlotsWithFilters_VariousFilters(String pointId, String date,
                                                         Integer minCap, String productCat, String expectedMethod) {
        List<TimeSlot> slots = Arrays.asList(testTimeSlot);

        switch(expectedMethod) {
            case "minCapacity":
                when(getAvailableTimeSlotsUseCase.getAvailableTimeSlotsWithMinCapacity(
                        pointId, LocalDate.parse(date), minCap)).thenReturn(slots);
                break;
            case "productCategory":
                when(getAvailableTimeSlotsUseCase.getAvailableTimeSlotsByProductCategory(
                        pointId, LocalDate.parse(date), productCat)).thenReturn(slots);
                break;
            default:
                when(getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointId, LocalDate.parse(date))).thenReturn(slots);
        }

        ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> response =
                scheduleController.getAvailableTimeSlotsWithFilters(pointId, date, minCap, productCat);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    private static Stream<Arguments> filterParameters() {
        return Stream.of(
                Arguments.of("point-001", "2024-01-15", 5, null, "minCapacity"),
                Arguments.of("point-001", "2024-01-15", null, "Desayuno", "productCategory"),
                Arguments.of("point-001", "2024-01-15", null, "", "default"),
                Arguments.of("point-001", "2024-01-15", null, null, "default")
        );
    }

    @Test
    void getAvailableTimeSlotsForNow_Success() {
        when(getAvailableTimeSlotsUseCase.getAvailableTimeSlotsForNow(any()))
                .thenReturn(List.of(testTimeSlot));

        ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> response =
                scheduleController.getAvailableTimeSlotsForNow("point-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getAvailableTimeSlotsWithClosuresValidation_Success() {
        when(getAvailableTimeSlotsUseCase.getAvailableTimeSlotsWithClosuresValidation(any(), any()))
                .thenReturn(List.of(testTimeSlot));

        ResponseEntity<SuccessResponse<List<TimeSlotResponse>>> response =
                scheduleController.getAvailableTimeSlotsWithClosuresValidation("point-001", "2024-01-15");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void createOperatingHours_Success() {
        CreateOperatingHoursRequest request = new CreateOperatingHoursRequest();
        request.setPointOfSaleId("point-001");
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setOpeningTime(LocalTime.of(8, 0));
        request.setClosingTime(LocalTime.of(18, 0));

        when(manageOperatingHoursUseCase.execute(any(CreateOperatingHoursCommand.class)))
                .thenReturn(testOperatingHours);

        ResponseEntity<OperatingHoursResponse> response = scheduleController.createOperatingHours(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-op-id", response.getBody().getId());
    }

    @Test
    void updateOperatingHours_Success() {
        String id = "test-op-id";
        CreateOperatingHoursRequest request = new CreateOperatingHoursRequest();
        request.setDayOfWeek(DayOfWeek.TUESDAY);
        request.setOpeningTime(LocalTime.of(9, 0));
        request.setClosingTime(LocalTime.of(17, 0));

        OperatingHours updated = createOperatingHours(id, "point-001",
                DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), true);

        when(manageOperatingHoursUseCase.execute(any(UpdateOperatingHoursCommand.class)))
                .thenReturn(updated);

        ResponseEntity<OperatingHoursResponse> response = scheduleController.updateOperatingHours(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DayOfWeek.TUESDAY, response.getBody().getDayOfWeek());
    }

    @Test
    void toggleOperatingHoursStatus_Success() {
        String id = "test-op-id";
        Boolean active = false;
        OperatingHours updated = createOperatingHours(id, "point-001",
                DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false);

        when(manageOperatingHoursUseCase.execute(any(ToggleOperatingHoursStatusCommand.class)))
                .thenReturn(updated);

        ResponseEntity<OperatingHoursResponse> response = scheduleController.toggleOperatingHoursStatus(id, active);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getActive());
    }

    @Test
    void getAllOperatingHours_Success() {
        List<OperatingHours> hours = Arrays.asList(testOperatingHours);
        when(manageOperatingHoursUseCase.getAllOperatingHours()).thenReturn(hours);

        ResponseEntity<List<OperatingHoursResponse>> response = scheduleController.getAllOperatingHours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getActiveOperatingHoursByPointOfSale_Success() {
        List<OperatingHours> hours = Arrays.asList(testOperatingHours);
        when(manageOperatingHoursUseCase.getActiveOperatingHoursByPointOfSale(any())).thenReturn(hours);

        ResponseEntity<List<OperatingHoursResponse>> response =
                scheduleController.getActiveOperatingHoursByPointOfSale("point-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllActiveOperatingHours_Success() {
        List<OperatingHours> hours = Arrays.asList(testOperatingHours);
        when(manageOperatingHoursUseCase.getAllActiveOperatingHours()).thenReturn(hours);

        ResponseEntity<List<OperatingHoursResponse>> response = scheduleController.getAllActiveOperatingHours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void createTemporaryClosure_Success() {
        CreateTemporaryClosureRequest request = new CreateTemporaryClosureRequest();
        request.setPointOfSaleId("point-001");
        request.setStartDateTime(LocalDateTime.now().plusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(2));
        request.setReason("Mantenimiento");

        when(manageTemporaryClosuresUseCase.execute(any(CreateTemporaryClosureCommand.class)))
                .thenReturn(testTemporaryClosure);

        ResponseEntity<TemporaryClosureResponse> response = scheduleController.createTemporaryClosure(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-closure-id", response.getBody().getId());
    }

    @Test
    void getTemporaryClosuresByPointOfSale_Success() {
        String pointId = "point-001";
        List<TemporaryClosure> closures = Arrays.asList(testTemporaryClosure);
        when(manageTemporaryClosuresUseCase.getClosuresByPointOfSale(pointId)).thenReturn(closures);

        ResponseEntity<List<TemporaryClosureResponse>> response =
                scheduleController.getTemporaryClosuresByPointOfSale(pointId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllTemporaryClosures_Success() {
        List<TemporaryClosure> closures = Arrays.asList(testTemporaryClosure);
        when(manageTemporaryClosuresUseCase.getAllTemporaryClosures()).thenReturn(closures);

        ResponseEntity<List<TemporaryClosureResponse>> response = scheduleController.getAllTemporaryClosures();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getActiveClosuresInRange_Success() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<TemporaryClosure> closures = Arrays.asList(testTemporaryClosure);

        when(manageTemporaryClosuresUseCase.getActiveClosuresInRange(any(), any())).thenReturn(closures);

        ResponseEntity<List<TemporaryClosureResponse>> response =
                scheduleController.getActiveClosuresInRange(start.toString(), end.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateTemporaryClosure_Success() {
        String id = "test-closure-id";
        CreateTemporaryClosureRequest request = new CreateTemporaryClosureRequest();
        request.setStartDateTime(LocalDateTime.now().plusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(2));
        request.setReason("Mantenimiento actualizado");

        TemporaryClosure updated = createTemporaryClosure(id, "point-001",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                "Mantenimiento actualizado", true);

        when(manageTemporaryClosuresUseCase.execute(any(UpdateTemporaryClosureCommand.class)))
                .thenReturn(updated);

        ResponseEntity<TemporaryClosureResponse> response = scheduleController.updateTemporaryClosure(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mantenimiento actualizado", response.getBody().getReason());
    }

    @Test
    void toggleTemporaryClosureStatus_Success() {
        String id = "test-closure-id";
        Boolean active = false;
        TemporaryClosure updated = createTemporaryClosure(id, "point-001",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                "Mantenimiento", false);

        when(manageTemporaryClosuresUseCase.execute(any(ToggleTemporaryClosureStatusCommand.class)))
                .thenReturn(updated);

        ResponseEntity<TemporaryClosureResponse> response = scheduleController.toggleTemporaryClosureStatus(id, active);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getActive());
    }

    @Test
    void deleteTemporaryClosure_Success() {
        doNothing().when(manageTemporaryClosuresUseCase).deleteTemporaryClosure(any());

        ResponseEntity<Void> response = scheduleController.deleteTemporaryClosure("test-closure-id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(manageTemporaryClosuresUseCase, times(1)).deleteTemporaryClosure("test-closure-id");
    }

    @Test
    void getPointOfSaleReport_Success() {
        String pointId = "point-001";
        Map<String, Object> data = Map.of("totalSlots", 10, "available", 5);
        when(scheduleReportsUseCase.generatePointOfSaleReport(pointId)).thenReturn(data);

        ResponseEntity<?> response = scheduleController.getPointOfSaleReport(pointId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void generateAvailabilityReport_Success() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        Map<String, Object> data = Map.of("availability", "80%");

        when(scheduleReportsUseCase.generateAvailabilityReport(any(), any())).thenReturn(data);

        ResponseEntity<?> response = scheduleController.generateAvailabilityReport(start.toString(), end.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void generateCategoryScheduleReport_Success() {
        Map<String, Object> data = Map.of("totalCategories", 5);
        when(scheduleReportsUseCase.generateCategoryScheduleReport()).thenReturn(data);

        ResponseEntity<?> response = scheduleController.generateCategoryScheduleReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void generateTimeSlotOccupancyReport_Success() {
        String pointId = "point-001";
        Map<String, Object> data = Map.of("occupancy", "75%");
        when(scheduleReportsUseCase.generateTimeSlotOccupancyReport(any(), any(), any()))
                .thenReturn(data);

        ResponseEntity<SuccessResponse<ScheduleReportResponse>> response =
                scheduleController.generateTimeSlotOccupancyReport(pointId, "2024-01-01", "2024-01-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void generatePeakHoursReport_Success() {
        String pointId = "point-001";
        Map<String, Object> data = Map.of("peakHours", List.of("10:00", "12:00"));
        when(scheduleReportsUseCase.generatePeakHoursReport(any(), any())).thenReturn(data);

        ResponseEntity<SuccessResponse<ScheduleReportResponse>> response =
                scheduleController.generatePeakHoursReport(pointId, "2024-01-15");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void generateCapacityUtilizationReport_Success() {
        String pointId = "point-001";
        Map<String, Object> data = Map.of("utilization", "60%");
        when(scheduleReportsUseCase.generateCapacityUtilizationReport(any())).thenReturn(data);

        ResponseEntity<SuccessResponse<ScheduleReportResponse>> response =
                scheduleController.generateCapacityUtilizationReport(pointId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
    }
}
