package edu.dosw;

import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.CategoryScheduleRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.OperatingHoursRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.Port.TemporaryClosureRepositoryPort;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.*;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UseCasesAndCommandsTest {

    @Mock
    private CategoryScheduleRepositoryPort categoryScheduleRepositoryPort;

    @Mock
    private OperatingHoursRepositoryPort operatingHoursRepositoryPort;

    @Mock
    private TemporaryClosureRepositoryPort temporaryClosureRepositoryPort;

    @InjectMocks
    private ManageCategorySchedulesUseCase manageCategorySchedulesUseCase;

    @InjectMocks
    private ManageOperatingHoursUseCase manageOperatingHoursUseCase;

    @InjectMocks
    private ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;

    @InjectMocks
    private GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase;

    @InjectMocks
    private ValidateAvailabilityUseCase validateAvailabilityUseCase;

    @InjectMocks
    private ScheduleReportsUseCase scheduleReportsUseCase;


    @Test
    @DisplayName("CreateCategoryScheduleCommand - Constructor y Getters")
    void testCreateCategoryScheduleCommand() {
        String categoryName = "Electronics";
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        CreateCategoryScheduleCommand command = new CreateCategoryScheduleCommand(categoryName, startTime, endTime);
        assertEquals(categoryName, command.getCategoryName());
        assertEquals(startTime, command.getStartTime());
        assertEquals(endTime, command.getEndTime());
    }

    @Test
    @DisplayName("CreateOperatingHoursCommand - Constructor y Getters")
    void testCreateOperatingHoursCommand() {
        String pointOfSaleId = "STORE001";
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        CreateOperatingHoursCommand command = new CreateOperatingHoursCommand(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        assertEquals(pointOfSaleId, command.getPointOfSaleId());
        assertEquals(dayOfWeek, command.getDayOfWeek());
        assertEquals(openingTime, command.getOpeningTime());
        assertEquals(closingTime, command.getClosingTime());
    }

    @Test
    @DisplayName("CreateTemporaryClosureCommand - Constructor y Getters")
    void testCreateTemporaryClosureCommand() {
        String pointOfSaleId = "STORE001";
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
        String reason = "Maintenance";
        CreateTemporaryClosureCommand command = new CreateTemporaryClosureCommand(pointOfSaleId, startDateTime, endDateTime, reason);
        assertEquals(pointOfSaleId, command.getPointOfSaleId());
        assertEquals(startDateTime, command.getStartDateTime());
        assertEquals(endDateTime, command.getEndDateTime());
        assertEquals(reason, command.getReason());
    }


    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Crear categoría exitosa")
    void testCreateCategoryScheduleSuccess() {
        String categoryName = "Electronics";
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        CategorySchedule expectedSchedule = new CategorySchedule(categoryName, startTime, endTime);

        when(categoryScheduleRepositoryPort.findByCategoryName(categoryName)).thenReturn(Optional.empty());
        when(categoryScheduleRepositoryPort.save(any(CategorySchedule.class))).thenReturn(expectedSchedule);
        CategorySchedule result = manageCategorySchedulesUseCase.createCategorySchedule(categoryName, startTime, endTime);
        assertNotNull(result);
        assertEquals(categoryName, result.getCategoryName());
        verify(categoryScheduleRepositoryPort).findByCategoryName(categoryName);
        verify(categoryScheduleRepositoryPort).save(any(CategorySchedule.class));
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Crear categoría con hora inválida")
    void testCreateCategoryScheduleInvalidTime() {
        String categoryName = "Electronics";
        LocalTime startTime = LocalTime.of(19, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        assertThrows(BusinessException.class, () ->
                manageCategorySchedulesUseCase.createCategorySchedule(categoryName, startTime, endTime)
        );
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Crear categoría duplicada")
    void testCreateCategoryScheduleDuplicate() {
        String categoryName = "Electronics";
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        CategorySchedule existing = new CategorySchedule(categoryName, startTime, endTime);
        when(categoryScheduleRepositoryPort.findByCategoryName(categoryName)).thenReturn(Optional.of(existing));
        assertThrows(BusinessException.class, () ->
                manageCategorySchedulesUseCase.createCategorySchedule(categoryName, startTime, endTime)
        );
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Obtener categoría existente")
    void testGetCategoryScheduleExists() {

        String categoryName = "Electronics";
        CategorySchedule expected = new CategorySchedule(categoryName, LocalTime.of(9, 0), LocalTime.of(18, 0));
        when(categoryScheduleRepositoryPort.findByCategoryName(categoryName)).thenReturn(Optional.of(expected));
        Optional<CategorySchedule> result = manageCategorySchedulesUseCase.getCategorySchedule(categoryName);
        assertTrue(result.isPresent());
        assertEquals(categoryName, result.get().getCategoryName());
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Obtener categoría no existente")
    void testGetCategoryScheduleNotExists() {
        String categoryName = "NonExistent";
        when(categoryScheduleRepositoryPort.findByCategoryName(categoryName)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                manageCategorySchedulesUseCase.getCategorySchedule(categoryName)
        );
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Obtener todas las categorías")
    void testGetAllCategorySchedules() {
        List<CategorySchedule> expected = Arrays.asList(
                new CategorySchedule("Electronics", LocalTime.of(9, 0), LocalTime.of(18, 0)),
                new CategorySchedule("Clothing", LocalTime.of(10, 0), LocalTime.of(20, 0))
        );

        when(categoryScheduleRepositoryPort.findAll()).thenReturn(expected);
        List<CategorySchedule> result = manageCategorySchedulesUseCase.getAllCategorySchedules();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("ManageCategorySchedulesUseCase - Obtener todas las categorías vacías")
    void testGetAllCategorySchedulesEmpty() {
        when(categoryScheduleRepositoryPort.findAll()).thenReturn(Collections.emptyList());
        assertThrows(BusinessException.class, () ->
                manageCategorySchedulesUseCase.getAllCategorySchedules()
        );
    }


    @Test
    @DisplayName("ManageOperatingHoursUseCase - Crear horario operativo exitoso")
    void testCreateOperatingHoursSuccess() {
        String pointOfSaleId = "STORE001";
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        OperatingHours expected = new OperatingHours(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        when(operatingHoursRepositoryPort.save(any(OperatingHours.class))).thenReturn(expected);
        OperatingHours result = manageOperatingHoursUseCase.createOperatingHours(pointOfSaleId, dayOfWeek, openingTime, closingTime);
        assertNotNull(result);
        assertEquals(pointOfSaleId, result.getPointOfSaleId());
        verify(operatingHoursRepositoryPort).save(any(OperatingHours.class));
    }

    @Test
    @DisplayName("ManageOperatingHoursUseCase - Crear horario con tiempo inválido")
    void testCreateOperatingHoursInvalidTime() {
        String pointOfSaleId = "STORE001";
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime openingTime = LocalTime.of(21, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        assertThrows(BusinessException.class, () ->
                manageOperatingHoursUseCase.createOperatingHours(pointOfSaleId, dayOfWeek, openingTime, closingTime)
        );
    }

    @Test
    @DisplayName("ManageOperatingHoursUseCase - Obtener horarios por punto de venta")
    void testGetOperatingHoursByPointOfSale() {
        String pointOfSaleId = "STORE001";
        List<OperatingHours> expected = Arrays.asList(
                new OperatingHours(pointOfSaleId, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(20, 0)),
                new OperatingHours(pointOfSaleId, DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(20, 0))
        );

        when(operatingHoursRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(expected);
        List<OperatingHours> result = manageOperatingHoursUseCase.getOperatingHoursByPointOfSale(pointOfSaleId);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("ManageOperatingHoursUseCase - Obtener horarios por punto de venta no existente")
    void testGetOperatingHoursByPointOfSaleNotFound() {
        String pointOfSaleId = "NON_EXISTENT";
        when(operatingHoursRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(Collections.emptyList());
        assertThrows(BusinessException.class, () ->
                manageOperatingHoursUseCase.getOperatingHoursByPointOfSale(pointOfSaleId)
        );
    }

    @Test
    @DisplayName("ManageOperatingHoursUseCase - Eliminar horario operativo")
    void testDeleteOperatingHours() {

        String id = "OH123";
        OperatingHours existing = new OperatingHours("STORE001", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(20, 0));
        when(operatingHoursRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(operatingHoursRepositoryPort).deleteById(id);
        assertDoesNotThrow(() -> manageOperatingHoursUseCase.deleteOperatingHours(id));
        verify(operatingHoursRepositoryPort).deleteById(id);
    }

    @Test
    @DisplayName("ManageOperatingHoursUseCase - Eliminar horario no existente")
    void testDeleteOperatingHoursNotFound() {
        String id = "NON_EXISTENT";
        when(operatingHoursRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                manageOperatingHoursUseCase.deleteOperatingHours(id)
        );
    }


    @Test
    @DisplayName("ManageTemporaryClosuresUseCase - Crear cierre temporal exitoso")
    void testCreateTemporaryClosureSuccess() {
        String pointOfSaleId = "STORE001";
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
        String reason = "Maintenance";
        TemporaryClosure expected = new TemporaryClosure(pointOfSaleId, startDateTime, endDateTime, reason);
        when(temporaryClosureRepositoryPort.save(any(TemporaryClosure.class))).thenReturn(expected);
        TemporaryClosure result = manageTemporaryClosuresUseCase.createTemporaryClosure(pointOfSaleId, startDateTime, endDateTime, reason);
        assertNotNull(result);
        assertEquals(pointOfSaleId, result.getPointOfSaleId());
        verify(temporaryClosureRepositoryPort).save(any(TemporaryClosure.class));
    }

    @Test
    @DisplayName("ManageTemporaryClosuresUseCase - Crear cierre temporal con fecha inválida")
    void testCreateTemporaryClosureInvalidDate() {
        String pointOfSaleId = "STORE001";
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);
        String reason = "Maintenance";
        assertThrows(BusinessException.class, () ->
                manageTemporaryClosuresUseCase.createTemporaryClosure(pointOfSaleId, startDateTime, endDateTime, reason)
        );
    }

    @Test
    @DisplayName("ManageTemporaryClosuresUseCase - Obtener cierres activos por punto de venta")
    void testGetActiveClosuresByPointOfSale() {
        String pointOfSaleId = "STORE001";
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        List<TemporaryClosure> expected = Arrays.asList(
                new TemporaryClosure(pointOfSaleId, dateTime.minusHours(1), dateTime.plusHours(1), "Maintenance")
        );

        when(temporaryClosureRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(expected);
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, dateTime)).thenReturn(expected);
        List<TemporaryClosure> result = manageTemporaryClosuresUseCase.getActiveClosuresByPointOfSale(pointOfSaleId, dateTime);
        assertFalse(result.isEmpty());
    }


    @Test
    @DisplayName("GetAvailableTimeSlotsUseCase - Obtener slots disponibles exitoso")
    void testGetAvailableTimeSlotsSuccess() {
        String pointOfSaleId = "STORE001";
        LocalDate date = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(17, 0));
        List<OperatingHours> operatingHoursList = Arrays.asList(operatingHours);
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek)).thenReturn(operatingHoursList);
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(anyString(), any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        List<TimeSlot> result = getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointOfSaleId, date);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("GetAvailableTimeSlotsUseCase - Obtener slots con fecha pasada")
    void testGetAvailableTimeSlotsPastDate() {
        String pointOfSaleId = "STORE001";
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThrows(BusinessException.class, () -> getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointOfSaleId, pastDate)
        );
    }

    @Test
    @DisplayName("GetAvailableTimeSlotsUseCase - Obtener slots sin horarios configurados")
    void testGetAvailableTimeSlotsNoOperatingHours() {
        String pointOfSaleId = "STORE001";
        LocalDate date = LocalDate.now().plusDays(1);
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, date.getDayOfWeek())).thenReturn(Collections.emptyList());
        assertThrows(BusinessException.class, () -> getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointOfSaleId, date)
        );
    }


    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar disponibilidad exitosa")
    void testValidatePointOfSaleAvailabilitySuccess() {
        String pointOfSaleId = "STORE001";
        LocalDateTime requestedTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0);
        DayOfWeek dayOfWeek = requestedTime.getDayOfWeek();

        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(17, 0));
        List<OperatingHours> operatingHoursList = Arrays.asList(operatingHours);
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime)).thenReturn(Collections.emptyList());
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek)).thenReturn(operatingHoursList);
        AvailabilityResult result = validateAvailabilityUseCase.validatePointOfSaleAvailability(pointOfSaleId, requestedTime);
        assertTrue(result.getAvailable());
        assertEquals("Disponible", result.getReason());
    }

    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar disponibilidad con cierre temporal")
    void testValidatePointOfSaleAvailabilityWithClosure() {
        String pointOfSaleId = "STORE001";
        LocalDateTime requestedTime = LocalDateTime.now().plusDays(1);
        TemporaryClosure closure = new TemporaryClosure(pointOfSaleId, requestedTime.minusHours(1), requestedTime.plusHours(1), "Maintenance");
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime)).thenReturn(Arrays.asList(closure));
        AvailabilityResult result = validateAvailabilityUseCase.validatePointOfSaleAvailability(pointOfSaleId, requestedTime);
        assertFalse(result.getAvailable());
        assertEquals("Punto de venta cerrado temporalmente", result.getReason());
    }

    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar disponibilidad fuera de horario")
    void testValidatePointOfSaleAvailabilityOutsideHours() {
        String pointOfSaleId = "STORE001";
        LocalDateTime requestedTime = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0);
        DayOfWeek dayOfWeek = requestedTime.getDayOfWeek();
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(17, 0));
        List<OperatingHours> operatingHoursList = Arrays.asList(operatingHours);
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime)).thenReturn(Collections.emptyList());
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek)).thenReturn(operatingHoursList);
        AvailabilityResult result = validateAvailabilityUseCase.validatePointOfSaleAvailability(pointOfSaleId, requestedTime);
        assertFalse(result.getAvailable());
        assertEquals("Fuera del horario de atención", result.getReason());
    }

    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar disponibilidad de categoría exitosa")
    void testValidateProductCategoryAvailabilitySuccess() {
        String pointOfSaleId = "STORE001";
        String productCategory = "Electronics";
        LocalDateTime requestedTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0);
        CategorySchedule categorySchedule = new CategorySchedule(productCategory, LocalTime.of(9, 0), LocalTime.of(18, 0));
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, requestedTime.getDayOfWeek(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime)).thenReturn(Collections.emptyList());
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, requestedTime.getDayOfWeek())).thenReturn(Arrays.asList(operatingHours));
        when(categoryScheduleRepositoryPort.findByCategoryName(productCategory)).thenReturn(Optional.of(categorySchedule));
        AvailabilityResult result = validateAvailabilityUseCase.validateProductCategoryAvailability(pointOfSaleId, requestedTime, productCategory);
        assertTrue(result.getAvailable());
        assertEquals("Disponible", result.getReason());
    }

    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar disponibilidad de categoría fuera de horario")
    void testValidateProductCategoryAvailabilityOutsideCategoryHours() {
        String pointOfSaleId = "STORE001";
        String productCategory = "Electronics";
        LocalDateTime requestedTime = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0);
        CategorySchedule categorySchedule = new CategorySchedule(productCategory, LocalTime.of(9, 0), LocalTime.of(18, 0));
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, requestedTime.getDayOfWeek(), LocalTime.of(8, 0), LocalTime.of(20, 0));
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(pointOfSaleId, requestedTime)).thenReturn(Collections.emptyList());
        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, requestedTime.getDayOfWeek())).thenReturn(Arrays.asList(operatingHours));
        when(categoryScheduleRepositoryPort.findByCategoryName(productCategory)).thenReturn(Optional.of(categorySchedule));
        AvailabilityResult result = validateAvailabilityUseCase.validateProductCategoryAvailability(pointOfSaleId, requestedTime, productCategory);
        assertFalse(result.getAvailable());
        assertEquals("Producto fuera de horario", result.getReason());
    }


    @Test
    @DisplayName("ScheduleReportsUseCase - Generar reporte exitoso")
    void testGeneratePointOfSaleReportSuccess() {
        String pointOfSaleId = "STORE001";
        List<OperatingHours> operatingHours = Arrays.asList(
                new OperatingHours(pointOfSaleId, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(20, 0))
        );
        List<TemporaryClosure> closures = Arrays.asList(
                new TemporaryClosure(pointOfSaleId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Maintenance")
        );

        when(operatingHoursRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(operatingHours);
        when(temporaryClosureRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(closures);
        Map<String, Object> report = scheduleReportsUseCase.generatePointOfSaleReport(pointOfSaleId);
        assertNotNull(report);
        assertEquals(pointOfSaleId, report.get("pointOfSaleId"));
        assertEquals(operatingHours, report.get("operatingHours"));
        assertEquals(closures, report.get("temporaryClosures"));
        assertEquals(1, report.get("closureCount"));
    }

    @Test
    @DisplayName("ScheduleReportsUseCase - Generar reporte para punto de venta no existente")
    void testGeneratePointOfSaleReportNotFound() {
        String pointOfSaleId = "NON_EXISTENT";
        when(operatingHoursRepositoryPort.findByPointOfSaleId(pointOfSaleId)).thenReturn(Collections.emptyList());
        assertThrows(BusinessException.class, () -> scheduleReportsUseCase.generatePointOfSaleReport(pointOfSaleId)
        );
    }

    @Test
    @DisplayName("ValidateAvailabilityUseCase - Validar con fecha pasada")
    void testValidateAvailabilityPastDate() {
        String pointOfSaleId = "STORE001";
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        assertThrows(BusinessException.class, () -> validateAvailabilityUseCase.validatePointOfSaleAvailability(pointOfSaleId, pastDateTime)
        );
    }

    @Test
    @DisplayName("GetAvailableTimeSlotsUseCase - Slots con cierres temporales")
    void testGetAvailableTimeSlotsWithClosures() {
        String pointOfSaleId = "STORE001";
        LocalDate date = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        OperatingHours operatingHours = new OperatingHours(pointOfSaleId, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(17, 0));
        List<OperatingHours> operatingHoursList = Arrays.asList(operatingHours);
        TemporaryClosure closure = new TemporaryClosure(pointOfSaleId,
                LocalDateTime.of(date, LocalTime.of(10, 0)),
                LocalDateTime.of(date, LocalTime.of(11, 0)),
                "Maintenance");

        when(operatingHoursRepositoryPort.findByPointOfSaleIdAndDayOfWeek(pointOfSaleId, dayOfWeek)).thenReturn(operatingHoursList);
        when(temporaryClosureRepositoryPort.findActiveClosuresByPointOfSaleAndDateTime(eq(pointOfSaleId), any(LocalDateTime.class))).thenAnswer(invocation -> {
                    LocalDateTime slotTime = invocation.getArgument(1);
                    if (slotTime.getHour() == 10) {
                        return Arrays.asList(closure);
                    }
                    return Collections.emptyList();
                });

        List<TimeSlot> result = getAvailableTimeSlotsUseCase.getAvailableTimeSlots(pointOfSaleId, date);
        assertNotNull(result);
        boolean has10amSlot = result.stream()
                .anyMatch(slot -> slot.getStartTime().getHour() == 10);
        assertFalse(has10amSlot);
    }
}