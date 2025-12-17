package edu.dosw;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.commands.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UseCasesAndCommandsTest {

    @Mock private OperatingHoursRepositoryPort operatingHoursRepository;
    @Mock private TemporaryClosureRepositoryPort temporaryClosureRepository;
    @Mock private CategoryScheduleRepositoryPort categoryScheduleRepository;
    @Mock private TimeSlotRepositoryPort timeSlotRepository;

    @InjectMocks private ManageOperatingHoursUseCase manageOperatingHoursUseCase;
    @InjectMocks private ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;
    @InjectMocks private ManageCategorySchedulesUseCase manageCategorySchedulesUseCase;
    @InjectMocks private CreateTimeSlotUseCase createTimeSlotUseCase;
    @InjectMocks private ReserveTimeSlotUseCase reserveTimeSlotUseCase;
    @InjectMocks private ReleaseTimeSlotUseCase releaseTimeSlotUseCase;
    @InjectMocks private ValidateAvailabilityUseCase validateAvailabilityUseCase;

    private CategorySchedule testCategory;
    private OperatingHours testOperatingHours;
    private TimeSlot testTimeSlot;
    private LocalDateTime futureDateTime;

    @BeforeEach
    void setUp() {
        futureDateTime = LocalDateTime.now().plusHours(2);

        testCategory = new CategorySchedule("Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30));
        testCategory.setId("cat-001");
        testCategory.setActive(true);

        testOperatingHours = new OperatingHours("point-001", DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        testOperatingHours.setId("oh-001");
        testOperatingHours.setActive(true);

        testTimeSlot = TimeSlot.builder()
                .id("slot-001")
                .pointOfSaleId("point-001")
                .startTime(futureDateTime)
                .endTime(futureDateTime.plusHours(1))
                .availableCapacity(10)
                .bookedCount(0)
                .available(true)
                .build();
    }

    @Test @Order(1)
    void createCategoryScheduleCommand_Valid() {
        CreateCategoryScheduleCommand cmd = new CreateCategoryScheduleCommand(
                "Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30));
        assertEquals("Desayuno", cmd.getCategoryName());
    }

    @Test @Order(2)
    void createCategoryScheduleCommand_InvalidTimes() {
        BusinessException ex = assertThrows(BusinessException.class, () ->
                new CreateCategoryScheduleCommand("Desayuno", LocalTime.of(12, 0), LocalTime.of(11, 30)));
        assertTrue(ex.getMessage().contains("hora de inicio"));
    }

    @Test @Order(3)
    void createOperatingHoursCommand_Valid() {
        CreateOperatingHoursCommand cmd = new CreateOperatingHoursCommand(
                "point-001", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0));
        assertEquals("point-001", cmd.getPointOfSaleId());
    }

    @Test @Order(4)
    void createOperatingHoursCommand_InvalidTimes() {
        BusinessException ex = assertThrows(BusinessException.class, () ->
                new CreateOperatingHoursCommand("point-001", DayOfWeek.MONDAY,
                        LocalTime.of(18, 0), LocalTime.of(8, 0)));
        assertTrue(ex.getMessage().contains("hora de apertura"));
    }

    @Test @Order(5)
    void createTimeSlotCommand_Validate() {
        CreateTimeSlotCommand cmd = new CreateTimeSlotCommand(
                "point-001", futureDateTime, futureDateTime.plusHours(1), 10);
        assertDoesNotThrow(cmd::validate);
    }

    @Test @Order(6)
    void createTimeSlotCommand_PastStart() {
        CreateTimeSlotCommand cmd = new CreateTimeSlotCommand(
                "point-001", LocalDateTime.now().minusHours(1), futureDateTime, 10);
        BusinessException ex = assertThrows(BusinessException.class, cmd::validate);
        assertTrue(ex.getMessage().contains("No se puede crear un slot en el pasado"));
    }

    @Test @Order(7)
    void reserveTimeSlotCommand_Valid() {
        ReserveTimeSlotCommand cmd = new ReserveTimeSlotCommand("slot-001", "order-001", "user-001");
        cmd.validate();
        assertEquals("slot-001", cmd.getSlotId());
    }

    @Test @Order(8)
    void reserveTimeSlotCommand_EmptyOrderId() {
        ReserveTimeSlotCommand cmd = new ReserveTimeSlotCommand("slot-001", "", "user-001");
        BusinessException ex = assertThrows(BusinessException.class, cmd::validate);
        assertTrue(ex.getMessage().contains("ID del pedido"));
    }

    @Test @Order(9)
    void createCategorySchedule_Success() {
        CreateCategoryScheduleCommand cmd = new CreateCategoryScheduleCommand(
                "Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30));

        when(categoryScheduleRepository.findByCategoryName("Desayuno")).thenReturn(Optional.empty());
        when(categoryScheduleRepository.save(any())).thenReturn(testCategory);

        CategorySchedule result = manageCategorySchedulesUseCase.execute(cmd);
        assertNotNull(result);
        assertEquals("Desayuno", result.getCategoryName());
    }

    @Test @Order(10)
    void createCategorySchedule_Duplicate() {
        CreateCategoryScheduleCommand cmd = new CreateCategoryScheduleCommand(
                "Desayuno", LocalTime.of(7, 0), LocalTime.of(11, 30));

        when(categoryScheduleRepository.findByCategoryName("Desayuno"))
                .thenReturn(Optional.of(testCategory));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> manageCategorySchedulesUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("Ya existe una categoría"));
    }

    @Test @Order(11)
    void toggleCategoryStatus_Success() {
        ToggleCategoryStatusCommand cmd = new ToggleCategoryStatusCommand("cat-001", false);

        when(categoryScheduleRepository.findById("cat-001"))
                .thenReturn(Optional.of(testCategory));
        when(categoryScheduleRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        CategorySchedule result = manageCategorySchedulesUseCase.execute(cmd);
        assertFalse(result.getActive());
    }

    @Test @Order(12)
    void deleteCategorySchedule_Success() {
        when(categoryScheduleRepository.findById("cat-001"))
                .thenReturn(Optional.of(testCategory));
        doNothing().when(categoryScheduleRepository).deleteById("cat-001");

        assertDoesNotThrow(() -> manageCategorySchedulesUseCase.deleteCategorySchedule("cat-001"));
        verify(categoryScheduleRepository).deleteById("cat-001");
    }

    @Test @Order(13)
    void createOperatingHours_Success() {
        CreateOperatingHoursCommand cmd = new CreateOperatingHoursCommand(
                "point-001", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0));

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", DayOfWeek.MONDAY))
                .thenReturn(Collections.emptyList());
        when(operatingHoursRepository.save(any())).thenReturn(testOperatingHours);

        OperatingHours result = manageOperatingHoursUseCase.execute(cmd);
        assertNotNull(result);
        assertEquals("point-001", result.getPointOfSaleId());
    }

    @Test @Order(14)
    void updateOperatingHours_NotFound() {
        UpdateOperatingHoursCommand cmd = new UpdateOperatingHoursCommand(
                "non-existent", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));

        when(operatingHoursRepository.findById("non-existent")).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> manageOperatingHoursUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test @Order(15)
    void getAllOperatingHours_Success() {
        when(operatingHoursRepository.findAll()).thenReturn(List.of(testOperatingHours));
        List<OperatingHours> result = manageOperatingHoursUseCase.getAllOperatingHours();
        assertFalse(result.isEmpty());
    }

    @Test @Order(16)
    void createTimeSlot_Success() {
        CreateTimeSlotCommand cmd = new CreateTimeSlotCommand(
                "point-001", futureDateTime, futureDateTime.plusHours(1), 10);

        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(eq("point-001"), any(), any()))
                .thenReturn(Collections.emptyList());
        when(timeSlotRepository.save(any())).thenReturn(testTimeSlot);

        TimeSlot result = createTimeSlotUseCase.execute(cmd);
        assertNotNull(result);
        verify(timeSlotRepository).save(any());
    }

    @Test @Order(17)
    void createTimeSlot_Overlap() {
        CreateTimeSlotCommand cmd = new CreateTimeSlotCommand(
                "point-001", futureDateTime, futureDateTime.plusHours(1), 10);

        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(eq("point-001"), any(), any()))
                .thenReturn(List.of(testTimeSlot));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> createTimeSlotUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("se solapa"));
    }

    @Test @Order(18)
    void reserveTimeSlot_Success() {
        ReserveTimeSlotCommand cmd = new ReserveTimeSlotCommand("slot-001", "order-001", "user-001");

        when(timeSlotRepository.findById("slot-001")).thenReturn(Optional.of(testTimeSlot));
        when(timeSlotRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TimeSlot result = reserveTimeSlotUseCase.execute(cmd);
        assertEquals(1, result.getBookedCount());
    }

    @Test @Order(19)
    void reserveTimeSlot_SlotNotFound() {
        ReserveTimeSlotCommand cmd = new ReserveTimeSlotCommand("non-existent", "order-001", "user-001");

        when(timeSlotRepository.findById("non-existent")).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> reserveTimeSlotUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test @Order(20)
    void releaseTimeSlot_Success() {
        ReleaseTimeSlotCommand cmd = new ReleaseTimeSlotCommand("slot-001", "order-001");

        TimeSlot reservedSlot = TimeSlot.builder()
                .id("slot-001")
                .availableCapacity(10)
                .bookedCount(1)
                .available(true)
                .build();

        when(timeSlotRepository.findById("slot-001")).thenReturn(Optional.of(reservedSlot));
        when(timeSlotRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TimeSlot result = releaseTimeSlotUseCase.execute(cmd);
        assertEquals(0, result.getBookedCount());
    }

    @Test @Order(21)
    void validateAvailability_Success() {
        LocalDateTime checkTime = futureDateTime;

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", checkTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));
        when(temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime("point-001", checkTime))
                .thenReturn(Collections.emptyList());
        when(categoryScheduleRepository.findActiveByCategoryName("Desayuno"))
                .thenReturn(Optional.of(testCategory));
        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(eq("point-001"), any(), any()))
                .thenReturn(List.of(testTimeSlot));

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailability(
                "point-001", checkTime, "Desayuno");
        assertNotNull(result);
    }

    @Test @Order(22)
    void validateAvailability_TemporaryClosure() {
        LocalDateTime checkTime = futureDateTime;

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", checkTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));

        TemporaryClosure closure = new TemporaryClosure("point-001",
                checkTime.minusHours(1), checkTime.plusHours(1), "Mantenimiento");
        closure.setActive(true);

        when(temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime("point-001", checkTime))
                .thenReturn(List.of(closure));

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailability(
                "point-001", checkTime, null);
        assertFalse(result.getAvailable());
        assertTrue(result.getReason().contains("cerrado temporalmente"));
    }

    @Test @Order(23)
    void validateAvailability_OutsideOperatingHours() {
        LocalDateTime checkTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(5, 0));

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", checkTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailability(
                "point-001", checkTime, null);
        assertFalse(result.getAvailable());
        assertTrue(result.getReason().contains("Fuera del horario"));
    }

    @Test @Order(24)
    void validateAvailability_PastDateTime() {
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> validateAvailabilityUseCase.validateAvailability("point-001", pastTime, null));
        assertTrue(ex.getMessage().contains("No se puede validar disponibilidad en fechas/horas pasadas"));
    }

    @Test @Order(25)
    void validateOrderAvailability_Success() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime checkTime = LocalDateTime.of(tomorrow, LocalTime.of(9, 30));

        System.out.println("\n=== DEBUG TEST ===");
        System.out.println("Check time: " + checkTime);

        reset(operatingHoursRepository, temporaryClosureRepository,
                categoryScheduleRepository, timeSlotRepository);

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", checkTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));
        System.out.println("Operating hours mocked: " + testOperatingHours.getOpeningTime() + " - " + testOperatingHours.getClosingTime());

        when(temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime("point-001", checkTime))
                .thenReturn(Collections.emptyList());

        when(categoryScheduleRepository.findActiveByCategoryName("Desayuno"))
                .thenReturn(Optional.of(testCategory));
        System.out.println("Category schedule mocked: " + testCategory.getStartTime() + " - " + testCategory.getEndTime());

        TimeSlot slot = TimeSlot.builder()
                .id("slot-001")
                .pointOfSaleId("point-001")
                .startTime(checkTime.minusMinutes(15))
                .endTime(checkTime.plusMinutes(15))
                .availableCapacity(10)
                .bookedCount(5)
                .available(true)
                .build();

        System.out.println("Slot created: " + slot.getStartTime() + " - " + slot.getEndTime());
        System.out.println("Slot available: " + slot.getAvailable());
        System.out.println("Slot isAvailable(): " + slot.isAvailable());
        System.out.println("Slot capacity: " + slot.getAvailableCapacity());
        System.out.println("Slot booked: " + slot.getBookedCount());

        boolean containsTime = !checkTime.isBefore(slot.getStartTime()) && !checkTime.isAfter(slot.getEndTime());
        System.out.println("Slot contains checkTime? " + containsTime);
        System.out.println("!checkTime.isBefore(slot.start): " + !checkTime.isBefore(slot.getStartTime()));
        System.out.println("!checkTime.isAfter(slot.end): " + !checkTime.isAfter(slot.getEndTime()));

        LocalDateTime searchStart = checkTime.minusMinutes(15);
        LocalDateTime searchEnd = checkTime.plusMinutes(45);
        System.out.println("Search range: " + searchStart + " to " + searchEnd);

        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(
                eq("point-001"),
                eq(searchStart),
                eq(searchEnd)))
                .thenReturn(List.of(slot));

        System.out.println("Calling validateOrderAvailability...");
        AvailabilityResult result = validateAvailabilityUseCase.validateOrderAvailability(
                "point-001", checkTime, List.of("Desayuno"));

        System.out.println("Result available: " + result.getAvailable());
        System.out.println("Result reason: " + result.getReason());
        System.out.println("Result category message: " + result.getCategoryMessage());
        System.out.println("=== END DEBUG ===\n");

        assertTrue(result.getAvailable(),
                "Debería estar disponible. Razón: " + result.getReason() +
                        ". Categoría: " + result.getCategoryMessage());
    }

    @Test @Order(26)
    void createTemporaryClosure_Success() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        CreateTemporaryClosureCommand cmd = new CreateTemporaryClosureCommand(
                "point-001", start, end, "Mantenimiento");

        TemporaryClosure closure = new TemporaryClosure("point-001", start, end, "Mantenimiento");
        closure.setId("tc-001");

        when(temporaryClosureRepository.save(any())).thenReturn(closure);

        TemporaryClosure result = manageTemporaryClosuresUseCase.execute(cmd);
        assertNotNull(result);
        assertEquals("point-001", result.getPointOfSaleId());
    }

    @Test @Order(27)
    void createTemporaryClosure_PastStart() {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        BusinessException ex = assertThrows(BusinessException.class, () ->
                new CreateTemporaryClosureCommand("point-001", past, past.plusHours(2), "Mantenimiento"));
        assertTrue(ex.getMessage().contains("No se puede crear un cierre temporal en el pasado"));
    }

    @Test @Order(28)
    void getActiveClosuresInRange_InvalidRange() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> manageTemporaryClosuresUseCase.getActiveClosuresInRange(start, end));
        assertTrue(ex.getMessage().contains("La fecha de inicio no puede ser después"));
    }

    @Test @Order(29)
    void createTimeSlot_ExactlySameTime() {
        CreateTimeSlotCommand cmd = new CreateTimeSlotCommand(
                "point-001",
                futureDateTime.plusSeconds(1),
                futureDateTime,
                10);

        BusinessException ex = assertThrows(BusinessException.class, cmd::validate);
        assertTrue(ex.getMessage().contains("hora de inicio no puede ser después"));
    }

    @Test @Order(30)
    void reserveTimeSlot_AlreadyFull() {
        ReserveTimeSlotCommand cmd = new ReserveTimeSlotCommand("slot-001", "order-001", "user-001");

        TimeSlot fullSlot = TimeSlot.builder()
                .id("slot-001")
                .availableCapacity(5)
                .bookedCount(5)
                .available(true)
                .build();

        when(timeSlotRepository.findById("slot-001")).thenReturn(Optional.of(fullSlot));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> reserveTimeSlotUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("no está disponible"));
    }

    @Test @Order(31)
    void releaseTimeSlot_AlreadyEmpty() {
        ReleaseTimeSlotCommand cmd = new ReleaseTimeSlotCommand("slot-001", "order-001");

        when(timeSlotRepository.findById("slot-001"))
                .thenReturn(Optional.of(testTimeSlot));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> releaseTimeSlotUseCase.execute(cmd));
        assertTrue(ex.getMessage().contains("no tiene reservas para liberar"));
    }

    @Test @Order(32)
    void completeFlow_ReserveAndReleaseSlot() {
        LocalDateTime slotTime = LocalDateTime.now().plusHours(2);

        ReserveTimeSlotCommand reserveCmd = new ReserveTimeSlotCommand("slot-001", "order-001", "user-001");

        TimeSlot slotForReservation = TimeSlot.builder()
                .id("slot-001")
                .pointOfSaleId("point-001")
                .startTime(slotTime)
                .endTime(slotTime.plusHours(1))
                .availableCapacity(10)
                .bookedCount(0)
                .available(true)
                .build();

        when(timeSlotRepository.findById("slot-001")).thenReturn(Optional.of(slotForReservation));
        when(timeSlotRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TimeSlot reservedSlot = reserveTimeSlotUseCase.execute(reserveCmd);
        assertEquals(1, reservedSlot.getBookedCount());

        ReleaseTimeSlotCommand releaseCmd = new ReleaseTimeSlotCommand("slot-001", "order-001");

        TimeSlot slotWithBooking = TimeSlot.builder()
                .id("slot-001")
                .pointOfSaleId("point-001")
                .startTime(slotTime)
                .endTime(slotTime.plusHours(1))
                .availableCapacity(10)
                .bookedCount(1)
                .available(true)
                .build();

        when(timeSlotRepository.findById("slot-001")).thenReturn(Optional.of(slotWithBooking));

        TimeSlot releasedSlot = releaseTimeSlotUseCase.execute(releaseCmd);
        assertEquals(0, releasedSlot.getBookedCount());
    }

    @Test @Order(33)
    void availabilityCheck_CompleteScenario() {
        LocalDateTime checkTime = futureDateTime;

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", checkTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));
        when(temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime("point-001", checkTime))
                .thenReturn(Collections.emptyList());
        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(eq("point-001"), any(), any()))
                .thenReturn(List.of(testTimeSlot));

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailability(
                "point-001", checkTime, null);
        assertNotNull(result);
    }

    @Test @Order(34)
    void integration_CategoryAndAvailability() {
        CreateCategoryScheduleCommand createCmd = new CreateCategoryScheduleCommand(
                "Almuerzo", LocalTime.of(12, 0), LocalTime.of(15, 0));

        CategorySchedule newCategory = new CategorySchedule("Almuerzo",
                LocalTime.of(12, 0), LocalTime.of(15, 0));
        newCategory.setId("cat-002");

        when(categoryScheduleRepository.findByCategoryName("Almuerzo")).thenReturn(Optional.empty());
        when(categoryScheduleRepository.save(any())).thenReturn(newCategory);

        CategorySchedule category = manageCategorySchedulesUseCase.execute(createCmd);
        assertNotNull(category);

        LocalDateTime lunchTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(13, 0));

        when(operatingHoursRepository.findByPointOfSaleIdAndDayOfWeek("point-001", lunchTime.getDayOfWeek()))
                .thenReturn(List.of(testOperatingHours));
        when(temporaryClosureRepository.findActiveClosuresByPointOfSaleAndDateTime("point-001", lunchTime))
                .thenReturn(Collections.emptyList());
        when(categoryScheduleRepository.findActiveByCategoryName("Almuerzo"))
                .thenReturn(Optional.of(newCategory));
        when(timeSlotRepository.findByPointOfSaleIdAndDateTimeRange(eq("point-001"), any(), any()))
                .thenReturn(List.of(testTimeSlot));

        AvailabilityResult result = validateAvailabilityUseCase.validateAvailability(
                "point-001", lunchTime, "Almuerzo");
        assertNotNull(result);
    }

    @Test @Order(35)
    void timeSlot_IsAvailable() {
        TimeSlot slot = TimeSlot.builder()
                .availableCapacity(10)
                .bookedCount(5)
                .available(true)
                .build();
        assertTrue(slot.isAvailable());

        slot.setBookedCount(10);
        assertFalse(slot.isAvailable());
    }

    @Test @Order(36)
    void timeSlot_ReserveAndRelease() {
        TimeSlot slot = TimeSlot.builder()
                .availableCapacity(10)
                .bookedCount(0)
                .available(true)
                .build();

        slot.reserveSlot();
        assertEquals(1, slot.getBookedCount());

        slot.releaseSlot();
        assertEquals(0, slot.getBookedCount());
    }

    @Test @Order(37)
    void timeSlot_ReserveWhenFull() {
        TimeSlot slot = TimeSlot.builder()
                .availableCapacity(10)
                .bookedCount(10)
                .available(true)
                .build();

        BusinessException ex = assertThrows(BusinessException.class, slot::reserveSlot);
        assertTrue(ex.getMessage().contains("Slot no disponible"));
    }

    @Test @Order(38)
    void availabilityResult_Constructor() {
        AvailabilityResult result = new AvailabilityResult(
                true, "point-001", futureDateTime, "Disponible");
        assertTrue(result.getAvailable());
        assertEquals("point-001", result.getPointOfSaleId());
    }


}