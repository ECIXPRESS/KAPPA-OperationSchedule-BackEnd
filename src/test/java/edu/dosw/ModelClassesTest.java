//package edu.dosw;
//
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import java.time.DayOfWeek;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
//class ModelClassesTest {
//
//    @Test
//    @DisplayName("AvailabilityResult - Constructor vacío y setters")
//    void testAvailabilityResultDefaultConstructorAndSetters() {
//        AvailabilityResult result = new AvailabilityResult();
//
//        result.setAvailable(true);
//        result.setPointOfSaleId("POS001");
//        result.setRequestedTime(LocalDateTime.of(2024, 1, 1, 10, 0));
//        result.setReason("Available");
//        result.setCategoryMessage("Category message");
//        List<String> timeSlots = Arrays.asList("10:00", "11:00");
//        result.setAvailableTimeSlots(timeSlots);
//
//        assertTrue(result.getAvailable());
//        assertEquals("POS001", result.getPointOfSaleId());
//        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), result.getRequestedTime());
//        assertEquals("Available", result.getReason());
//        assertEquals("Category message", result.getCategoryMessage());
//        assertEquals(timeSlots, result.getAvailableTimeSlots());
//    }
//
//    @Test
//    @DisplayName("AvailabilityResult - Constructor con parámetros")
//    void testAvailabilityResultParameterizedConstructor() {
//        LocalDateTime requestedTime = LocalDateTime.now();
//
//        AvailabilityResult result = new AvailabilityResult(false, "POS002", requestedTime, "Closed");
//
//        assertFalse(result.getAvailable());
//        assertEquals("POS002", result.getPointOfSaleId());
//        assertEquals(requestedTime, result.getRequestedTime());
//        assertEquals("Closed", result.getReason());
//        assertNotNull(result.getCategoryMessage());
//        assertNull(result.getAvailableTimeSlots());
//    }
//
//    @Test
//    @DisplayName("AvailabilityResult - Valores nulos y edge cases")
//    void testAvailabilityResultNullValuesAndEdgeCases() {
//        AvailabilityResult result = new AvailabilityResult(null, null, null, null);
//
//        assertNull(result.getAvailable());
//        assertNull(result.getPointOfSaleId());
//        assertNull(result.getRequestedTime());
//        assertNull(result.getReason());
//        assertEquals("", result.getCategoryMessage());
//        assertNull(result.getAvailableTimeSlots());
//
//        result.setAvailableTimeSlots(Arrays.asList());
//
//        assertNotNull(result.getAvailableTimeSlots());
//        assertTrue(result.getAvailableTimeSlots().isEmpty());
//    }
//
//    @Test
//    @DisplayName("CategorySchedule - Constructor vacío y setters")
//    void testCategoryScheduleDefaultConstructorAndSetters() {
//        CategorySchedule schedule = new CategorySchedule();
//
//        schedule.setId("CS123");
//        schedule.setCategoryName("Electronics");
//        schedule.setStartTime(LocalTime.of(9, 0));
//        schedule.setEndTime(LocalTime.of(18, 0));
//        schedule.setActive(false);
//
//        assertEquals("CS123", schedule.getId());
//        assertEquals("Electronics", schedule.getCategoryName());
//        assertEquals(LocalTime.of(9, 0), schedule.getStartTime());
//        assertEquals(LocalTime.of(18, 0), schedule.getEndTime());
//        assertFalse(schedule.getActive());
//    }
//
//    @Test
//    @DisplayName("CategorySchedule - Constructor con parámetros")
//    void testCategoryScheduleParameterizedConstructor() {
//        LocalTime startTime = LocalTime.of(8, 0);
//        LocalTime endTime = LocalTime.of(17, 0);
//
//        CategorySchedule schedule = new CategorySchedule("Clothing", startTime, endTime);
//
//        assertNotNull(schedule.getId());
//        assertTrue(schedule.getId().startsWith("CS"));
//        assertEquals("Clothing", schedule.getCategoryName());
//        assertEquals(startTime, schedule.getStartTime());
//        assertEquals(endTime, schedule.getEndTime());
//        assertTrue(schedule.getActive());
//    }
//
//    @Test
//    @DisplayName("CategorySchedule - Valores límite")
//    void testCategoryScheduleBoundaryValues() {
//        CategorySchedule schedule = new CategorySchedule("Test", LocalTime.MIN, LocalTime.MAX);
//
//        assertEquals(LocalTime.MIN, schedule.getStartTime());
//        assertEquals(LocalTime.MAX, schedule.getEndTime());
//        assertTrue(schedule.getActive());
//    }
//
//    @Test
//    @DisplayName("OperatingHours - Constructor vacío y setters")
//    void testOperatingHoursDefaultConstructorAndSetters() {
//        OperatingHours hours = new OperatingHours();
//
//        hours.setId("OH001");
//        hours.setPointOfSaleId("STORE001");
//        hours.setDayOfWeek(DayOfWeek.MONDAY);
//        hours.setOpeningTime(LocalTime.of(8, 0));
//        hours.setClosingTime(LocalTime.of(20, 0));
//        hours.setActive(false);
//
//        assertEquals("OH001", hours.getId());
//        assertEquals("STORE001", hours.getPointOfSaleId());
//        assertEquals(DayOfWeek.MONDAY, hours.getDayOfWeek());
//        assertEquals(LocalTime.of(8, 0), hours.getOpeningTime());
//        assertEquals(LocalTime.of(20, 0), hours.getClosingTime());
//        assertFalse(hours.getActive());
//    }
//
//    @Test
//    @DisplayName("OperatingHours - Constructor con parámetros")
//    void testOperatingHoursParameterizedConstructor() {
//        String posId = "STORE002";
//        DayOfWeek day = DayOfWeek.SATURDAY;
//        LocalTime opening = LocalTime.of(10, 0);
//        LocalTime closing = LocalTime.of(22, 0);
//
//        OperatingHours hours = new OperatingHours(posId, day, opening, closing);
//
//        assertNotNull(hours.getId());
//        assertTrue(hours.getId().startsWith("OH"));
//        assertEquals(posId, hours.getPointOfSaleId());
//        assertEquals(day, hours.getDayOfWeek());
//        assertEquals(opening, hours.getOpeningTime());
//        assertEquals(closing, hours.getClosingTime());
//        assertTrue(hours.getActive());
//    }
//
//    @Test
//    @DisplayName("OperatingHours - Todos los días de la semana")
//    void testOperatingHoursAllDaysOfWeek() {
//        for (DayOfWeek day : DayOfWeek.values()) {
//            OperatingHours hours = new OperatingHours("STORE", day, LocalTime.NOON, LocalTime.MIDNIGHT);
//
//            assertEquals(day, hours.getDayOfWeek());
//            assertTrue(hours.getActive());
//        }
//    }
//
//    @Test
//    @DisplayName("TemporaryClosure - Constructor vacío y setters")
//    void testTemporaryClosureDefaultConstructorAndSetters() {
//        TemporaryClosure closure = new TemporaryClosure();
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime start = now.plusDays(1);
//        LocalDateTime end = now.plusDays(2);
//
//        closure.setId("TC001");
//        closure.setPointOfSaleId("STORE001");
//        closure.setStartDateTime(start);
//        closure.setEndDateTime(end);
//        closure.setReason("Maintenance");
//        closure.setClosureType(TemporaryClosureType.HOLIDAY);
//        closure.setActive(false);
//        closure.setCreatedAt(now);
//
//        assertEquals("TC001", closure.getId());
//        assertEquals("STORE001", closure.getPointOfSaleId());
//        assertEquals(start, closure.getStartDateTime());
//        assertEquals(end, closure.getEndDateTime());
//        assertEquals("Maintenance", closure.getReason());
//        assertEquals(TemporaryClosureType.HOLIDAY, closure.getClosureType());
//        assertFalse(closure.getActive());
//        assertEquals(now, closure.getCreatedAt());
//    }
//
//    @Test
//    @DisplayName("TemporaryClosure - Constructor con parámetros")
//    void testTemporaryClosureParameterizedConstructor() {
//        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
//
//        TemporaryClosure closure = new TemporaryClosure("STORE002", start, end, "Inventory");
//
//        assertNotNull(closure.getId());
//        assertTrue(closure.getId().startsWith("TC"));
//        assertEquals("STORE002", closure.getPointOfSaleId());
//        assertEquals(start, closure.getStartDateTime());
//        assertEquals(end, closure.getEndDateTime());
//        assertEquals("Inventory", closure.getReason());
//        assertEquals(TemporaryClosureType.MAINTENANCE, closure.getClosureType());
//        assertTrue(closure.getActive());
//        assertNotNull(closure.getCreatedAt());
//    }
//
//    @Test
//    @DisplayName("TemporaryClosure - Todos los tipos de cierre")
//    void testTemporaryClosureAllClosureTypes() {
//        for (TemporaryClosureType type : TemporaryClosureType.values()) {
//            TemporaryClosure closure = new TemporaryClosure();
//
//            closure.setClosureType(type);
//
//            assertEquals(type, closure.getClosureType());
//        }
//    }
//
//    @Test
//    @DisplayName("TemporaryClosureType - Valores del enum")
//    void testTemporaryClosureTypeEnumValues() {
//        TemporaryClosureType[] values = TemporaryClosureType.values();
//
//        assertEquals(5, values.length);
//        assertEquals(TemporaryClosureType.MAINTENANCE, values[0]);
//        assertEquals(TemporaryClosureType.HOLIDAY, values[1]);
//        assertEquals(TemporaryClosureType.INVENTORY, values[2]);
//        assertEquals(TemporaryClosureType.TECHNICAL_ISSUE, values[3]);
//        assertEquals(TemporaryClosureType.OTHER, values[4]);
//    }
//
//    @Test
//    @DisplayName("TemporaryClosureType - valueOf")
//    void testTemporaryClosureTypeValueOf() {
//        assertEquals(TemporaryClosureType.MAINTENANCE, TemporaryClosureType.valueOf("MAINTENANCE"));
//        assertEquals(TemporaryClosureType.HOLIDAY, TemporaryClosureType.valueOf("HOLIDAY"));
//        assertEquals(TemporaryClosureType.INVENTORY, TemporaryClosureType.valueOf("INVENTORY"));
//        assertEquals(TemporaryClosureType.TECHNICAL_ISSUE, TemporaryClosureType.valueOf("TECHNICAL_ISSUE"));
//        assertEquals(TemporaryClosureType.OTHER, TemporaryClosureType.valueOf("OTHER"));
//    }
//
//    @Test
//    @DisplayName("TimeSlot - Constructor vacío y setters")
//    void testTimeSlotDefaultConstructorAndSetters() {
//        TimeSlot slot = new TimeSlot();
//        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 10, 0);
//
//        slot.setStartTime(start);
//        slot.setEndTime(end);
//        slot.setAvailableCapacity(5);
//        slot.setAvailable(false);
//
//        assertEquals(start, slot.getStartTime());
//        assertEquals(end, slot.getEndTime());
//        assertEquals(5, slot.getAvailableCapacity());
//        assertFalse(slot.getAvailable());
//    }
//
//    @Test
//    @DisplayName("TimeSlot - Constructor con parámetros")
//    void testTimeSlotParameterizedConstructor() {
//        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 11, 0);
//
//        TimeSlot slot = new TimeSlot(start, end, 10);
//
//        assertEquals(start, slot.getStartTime());
//        assertEquals(end, slot.getEndTime());
//        assertEquals(10, slot.getAvailableCapacity());
//        assertTrue(slot.getAvailable());
//    }
//
//    @Test
//    @DisplayName("TimeSlot - Capacidad cero y negativa")
//    void testTimeSlotZeroAndNegativeCapacity() {
//        TimeSlot slot = new TimeSlot();
//
//        slot.setAvailableCapacity(0);
//        assertEquals(0, slot.getAvailableCapacity());
//
//        slot.setAvailableCapacity(-1);
//        assertEquals(-1, slot.getAvailableCapacity());
//    }
//
//    @Test
//    @DisplayName("TimeSlot - Valores por defecto en constructor")
//    void testTimeSlotDefaultValues() {
//        TimeSlot slot = new TimeSlot();
//
//        assertNull(slot.getStartTime());
//        assertNull(slot.getEndTime());
//        assertNull(slot.getAvailableCapacity());
//        assertTrue(slot.getAvailable());
//    }
//
//    @Test
//    @DisplayName("AvailabilityResult - Lista de time slots nula")
//    void testAvailabilityResultNullTimeSlots() {
//        AvailabilityResult result = new AvailabilityResult(true, "POS003", LocalDateTime.now(), "Test");
//
//        result.setAvailableTimeSlots(null);
//
//        assertNull(result.getAvailableTimeSlots());
//    }
//
//    @Test
//    @DisplayName("CategorySchedule - Activación/Desactivación")
//    void testCategoryScheduleActivation() {
//        CategorySchedule schedule = new CategorySchedule("Test", LocalTime.of(9, 0), LocalTime.of(17, 0));
//
//        assertTrue(schedule.getActive());
//
//        schedule.setActive(false);
//        assertFalse(schedule.getActive());
//
//        schedule.setActive(true);
//        assertTrue(schedule.getActive());
//    }
//
//    @Test
//    @DisplayName("OperatingHours - Horarios extremos")
//    void testOperatingHoursExtremeTimes() {
//        OperatingHours hours = new OperatingHours("STORE", DayOfWeek.SUNDAY, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT);
//
//        assertEquals(LocalTime.MIDNIGHT, hours.getOpeningTime());
//        assertEquals(LocalTime.MIDNIGHT, hours.getClosingTime());
//    }
//
//    @Test
//    @DisplayName("TemporaryClosure - Fechas idénticas")
//    void testTemporaryClosureSameDates() {
//        LocalDateTime sameDateTime = LocalDateTime.now();
//        TemporaryClosure closure = new TemporaryClosure("STORE", sameDateTime, sameDateTime, "Test");
//
//        assertEquals(sameDateTime, closure.getStartDateTime());
//        assertEquals(sameDateTime, closure.getEndDateTime());
//    }
//
//    @Test
//    @DisplayName("TimeSlot - Cambio de disponibilidad")
//    void testTimeSlotAvailabilityChange() {
//        TimeSlot slot = new TimeSlot(LocalDateTime.now(), LocalDateTime.now().plusHours(1), 5);
//
//        assertTrue(slot.getAvailable());
//
//        slot.setAvailable(false);
//        assertFalse(slot.getAvailable());
//
//        slot.setAvailable(true);
//        assertTrue(slot.getAvailable());
//    }
//}