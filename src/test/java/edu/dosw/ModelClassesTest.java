package edu.dosw;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelClassesTest {

    @Test
    void testAvailabilityResultConstructorsAndSetters() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 15, 10, 30);
        AvailabilityResult result1 = new AvailabilityResult(true, "POS-001", time, "Test");
        assertTrue(result1.getAvailable());
        assertEquals("POS-001", result1.getPointOfSaleId());
        assertEquals(time, result1.getRequestedTime());
        assertEquals("Test", result1.getReason());
        assertEquals("", result1.getCategoryMessage());
        assertNull(result1.getAvailableTimeSlots());

        List<String> slots = Arrays.asList("10:00", "11:00");
        AvailabilityResult result2 = new AvailabilityResult(false, "POS-002", time, "Closed",
                "Category", slots);
        assertFalse(result2.getAvailable());
        assertEquals("POS-002", result2.getPointOfSaleId());
        assertEquals("Closed", result2.getReason());
        assertEquals("Category", result2.getCategoryMessage());
        assertEquals(slots, result2.getAvailableTimeSlots());

        AvailabilityResult result3 = new AvailabilityResult();
        result3.setAvailable(true);
        result3.setPointOfSaleId("POS-003");
        result3.setCategoryMessage("Test");
        assertTrue(result3.getAvailable());
        assertEquals("POS-003", result3.getPointOfSaleId());
        assertEquals("Test", result3.getCategoryMessage());
    }

    @Test
    void testCategoryScheduleConstructors() {
        CategorySchedule schedule1 = new CategorySchedule("Electrónicos",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0));
        assertEquals("Electrónicos", schedule1.getCategoryName());
        assertEquals(LocalTime.of(9, 0), schedule1.getStartTime());
        assertEquals(LocalTime.of(18, 0), schedule1.getEndTime());
        assertTrue(schedule1.getActive());

        CategorySchedule schedule2 = new CategorySchedule("ID-1", "Alimentos",
                LocalTime.of(8, 0),
                LocalTime.of(22, 0), false);
        assertEquals("ID-1", schedule2.getId());
        assertEquals("Alimentos", schedule2.getCategoryName());
        assertFalse(schedule2.getActive());
    }

    @Test
    void testOperatingHoursConstructors() {
        OperatingHours hours1 = new OperatingHours("POS-001", DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(20, 0));
        assertEquals("POS-001", hours1.getPointOfSaleId());
        assertEquals(DayOfWeek.MONDAY, hours1.getDayOfWeek());
        assertTrue(hours1.getActive());

        OperatingHours hours2 = new OperatingHours("ID-1", "POS-002", DayOfWeek.SUNDAY,
                LocalTime.of(12, 0),
                LocalTime.of(18, 0), false);
        assertEquals("ID-1", hours2.getId());
        assertFalse(hours2.getActive());
    }

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void testOperatingHoursAllDays(DayOfWeek day) {
        OperatingHours hours = new OperatingHours();
        hours.setDayOfWeek(day);
        assertEquals(day, hours.getDayOfWeek());
    }

    @Test
    void testTemporaryClosureConstructors() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 23, 59);

        TemporaryClosure closure1 = new TemporaryClosure("POS-001", start, end, "Mantenimiento");
        assertEquals("POS-001", closure1.getPointOfSaleId());
        assertEquals(start, closure1.getStartDateTime());
        assertEquals(end, closure1.getEndDateTime());
        assertEquals(TemporaryClosureType.MAINTENANCE, closure1.getClosureType());
        assertTrue(closure1.getActive());
        assertNotNull(closure1.getCreatedAt());

        TemporaryClosure closure2 = new TemporaryClosure("ID-1", "POS-002", start, end,
                "Inventario", TemporaryClosureType.INVENTORY,
                false, start.minusDays(1));
        assertEquals("ID-1", closure2.getId());
        assertEquals(TemporaryClosureType.INVENTORY, closure2.getClosureType());
        assertFalse(closure2.getActive());
    }

    @ParameterizedTest
    @EnumSource(TemporaryClosureType.class)
    void testAllClosureTypes(TemporaryClosureType type) {
        TemporaryClosure closure = new TemporaryClosure();
        closure.setClosureType(type);
        assertEquals(type, closure.getClosureType());
    }

    @Test
    void testTimeSlotConstructors() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 11, 0);

        TimeSlot slot1 = new TimeSlot(start, end, 5);
        assertEquals(start, slot1.getStartTime());
        assertEquals(end, slot1.getEndTime());
        assertEquals(5, slot1.getAvailableCapacity());

        TimeSlot slot2 = new TimeSlot("POS-001", start, end, 10);
        assertEquals("POS-001", slot2.getPointOfSaleId());
        assertEquals(10, slot2.getAvailableCapacity());
        assertTrue(slot2.getAvailable());
        assertEquals(0, slot2.getBookedCount());
    }

    @Test
    void testTimeSlotBuilder() {
        LocalDateTime start = LocalDateTime.now();
        TimeSlot slot = TimeSlot.builder()
                .id("SLOT-001")
                .pointOfSaleId("POS-001")
                .startTime(start)
                .endTime(start.plusHours(1))
                .availableCapacity(5)
                .bookedCount(2)
                .available(false)
                .build();

        assertEquals("SLOT-001", slot.getId());
        assertEquals("POS-001", slot.getPointOfSaleId());
        assertEquals(3, slot.getAvailableCapacity());
        assertEquals(2, slot.getBookedCount());
        assertFalse(slot.getAvailable());
    }

    @Test
    void testTimeSlotAvailability() {
        TimeSlot slot = new TimeSlot("POS-001", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 3);
        assertTrue(slot.isAvailable());
        assertEquals(3, slot.getAvailableCapacity());
        slot.setAvailable(false);
        assertFalse(slot.isAvailable());
        slot.setAvailable(true);
        slot.setBookedCount(3);
        assertFalse(slot.isAvailable());
        slot.setBookedCount(5);
        assertFalse(slot.isAvailable());
    }

    @Test
    void testTimeSlotReserveAndRelease() {
        TimeSlot slot = new TimeSlot("POS-001", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 3);

        slot.reserveSlot();
        assertEquals(1, slot.getBookedCount());
        assertEquals(2, slot.getAvailableCapacity());

        slot.reserveSlot();
        assertEquals(2, slot.getBookedCount());

        slot.releaseSlot();
        assertEquals(1, slot.getBookedCount());

        slot.setBookedCount(0);
        slot.releaseSlot();
        assertEquals(0, slot.getBookedCount());
    }

    @Test
    void testTimeSlotReserveWhenFullThrowsException() {
        TimeSlot slot = new TimeSlot("POS-001", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 1);
        slot.reserveSlot();

        BusinessException exception = assertThrows(BusinessException.class,
                slot::reserveSlot);
        assertTrue(exception.getMessage().contains("capacidad agotada"));
    }

    @Test
    void testTimeSlotContainsTime() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 11, 0);
        TimeSlot slot = new TimeSlot("POS-001", start, end, 5);

        assertTrue(slot.containsTime(LocalDateTime.of(2024, 1, 15, 10, 30)));
        assertTrue(slot.containsTime(start));
        assertTrue(slot.containsTime(end));
        assertFalse(slot.containsTime(LocalDateTime.of(2024, 1, 15, 9, 59)));
        assertFalse(slot.containsTime(LocalDateTime.of(2024, 1, 15, 11, 1)));
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T10:30,2024-01-15T11:30,true",
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T09:30,2024-01-15T10:30,true",
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T10:00,2024-01-15T11:00,true",
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T11:00,2024-01-15T12:00,true",
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T08:00,2024-01-15T09:00,false",
            "2024-01-15T10:00,2024-01-15T11:00,2024-01-15T11:01,2024-01-15T12:00,false"
    })
    void testTimeSlotOverlapsWith(String startStr, String endStr,
                                  String otherStartStr, String otherEndStr,
                                  boolean expected) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);
        LocalDateTime otherStart = LocalDateTime.parse(otherStartStr);
        LocalDateTime otherEnd = LocalDateTime.parse(otherEndStr);

        TimeSlot slot = new TimeSlot("POS-001", start, end, 5);
        assertEquals(expected, slot.overlapsWith(otherStart, otherEnd),
                String.format("Slot [%s-%s] vs [%s-%s]", start, end, otherStart, otherEnd));
    }

    @Test
    void testTimeSlotDeprecatedMethods() {
        TimeSlot slot = new TimeSlot("POS-001", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 5);
        slot.incrementBookings();
        assertEquals(1, slot.getBookedCount());

        slot.decrementBookings();
        assertEquals(0, slot.getBookedCount());
    }

    @Test
    void testBusinessExceptionConstructors() {
        BusinessException ex1 = new BusinessException("Error");
        assertEquals("Error", ex1.getMessage());
        assertEquals("BUSINESS_ERROR", ex1.getErrorCode());

        BusinessException ex2 = new BusinessException("Msg", "CODE",
                org.springframework.http.HttpStatus.CONFLICT);
        assertEquals("Msg", ex2.getMessage());
        assertEquals("CODE", ex2.getErrorCode());
    }

    @Test
    void testBusinessExceptionStaticMethods() {
        assertTrue(BusinessException.scheduleNotFound("ID").getMessage().contains("ID"));
        assertTrue(BusinessException.pointOfSaleNotFound("POS").getMessage().contains("POS"));
        assertEquals("SCHEDULE_CONFLICT", BusinessException.scheduleConflict("").getErrorCode());
        assertEquals("VALIDATION_ERROR", BusinessException.validationError("").getErrorCode());
        assertEquals("VALIDATION_ERROR", BusinessException.CapacityExceededException("").getErrorCode());
    }


    @Test
    void testErrorResponse() {
        ErrorResponse error = new ErrorResponse("CODE", "Message", "/path");
        assertEquals("CODE", error.getErrorCode());
        assertEquals("Message", error.getMessage());
        assertEquals("/path", error.getPath());
        assertNotNull(error.getTimestamp());

        error.setErrorCode("NEW");
        error.setMessage("New message");
        assertEquals("NEW", error.getErrorCode());
        assertEquals("New message", error.getMessage());
    }


    @Test
    void testTemporaryClosureTypeEnum() {
        TemporaryClosureType[] types = TemporaryClosureType.values();
        assertEquals(5, types.length);
        assertEquals(TemporaryClosureType.MAINTENANCE, TemporaryClosureType.valueOf("MAINTENANCE"));
        assertEquals(TemporaryClosureType.HOLIDAY, TemporaryClosureType.valueOf("HOLIDAY"));
        assertEquals(TemporaryClosureType.INVENTORY, TemporaryClosureType.valueOf("INVENTORY"));
        assertEquals(TemporaryClosureType.TECHNICAL_ISSUE, TemporaryClosureType.valueOf("TECHNICAL_ISSUE"));
        assertEquals(TemporaryClosureType.OTHER, TemporaryClosureType.valueOf("OTHER"));
    }
}