//package edu.dosw;
//
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Application.useCases.*;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.OperatingHours;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TemporaryClosure;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.AvailabilityResult;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model.TimeSlot;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.Controller.ScheduleController;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request.*;
//import edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.DayOfWeek;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ScheduleControllerTest {
//
//    @Mock
//    private ManageOperatingHoursUseCase manageOperatingHoursUseCase;
//
//    @Mock
//    private ManageTemporaryClosuresUseCase manageTemporaryClosuresUseCase;
//
//    @Mock
//    private ValidateAvailabilityUseCase validateAvailabilityUseCase;
//
//    @Mock
//    private GetAvailableTimeSlotsUseCase getAvailableTimeSlotsUseCase;
//
//    @Mock
//    private ScheduleReportsUseCase scheduleReportsUseCase;
//
//    @InjectMocks
//    private ScheduleController scheduleController;
//
//    @Test
//    void checkAvailability_ShouldReturnAvailabilityResponse() {
//        AvailabilityCheckRequest request = new AvailabilityCheckRequest();
//        request.setPointOfSaleId("pos1");
//        request.setRequestedTime(LocalDateTime.now());
//        request.setProductCategory("CATEGORY_A");
//
//        AvailabilityResult mockResult = mock(AvailabilityResult.class);
//        when(validateAvailabilityUseCase.validateProductCategoryAvailability(
//                eq("pos1"), any(LocalDateTime.class), eq("CATEGORY_A")))
//                .thenReturn(mockResult);
//        ResponseEntity<AvailabilityResponse> response = scheduleController.checkAvailability(request);
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        verify(validateAvailabilityUseCase).validateProductCategoryAvailability(
//                eq("pos1"), any(LocalDateTime.class), eq("CATEGORY_A"));
//    }
//
//    @Test
//    void checkAvailability_WhenServiceThrowsException_ShouldPropagateException() {
//        AvailabilityCheckRequest request = new AvailabilityCheckRequest();
//        request.setPointOfSaleId("pos1");
//        request.setRequestedTime(LocalDateTime.now());
//        request.setProductCategory("CATEGORY_A");
//        when(validateAvailabilityUseCase.validateProductCategoryAvailability(
//                anyString(), any(LocalDateTime.class), anyString()))
//                .thenThrow(new RuntimeException("Validation error"));
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            scheduleController.checkAvailability(request);
//        });
//
//        assertEquals("Validation error", exception.getMessage());
//    }
//
//    @Test
//    void getAvailableTimeSlots_ShouldReturnTimeSlots() {
//        String date = "2024-01-15";
//        LocalDate localDate = LocalDate.parse(date);
//
//        TimeSlot timeSlot1 = mock(TimeSlot.class);
//        TimeSlot timeSlot2 = mock(TimeSlot.class);
//        List<TimeSlot> mockTimeSlots = List.of(timeSlot1, timeSlot2);
//
//        when(getAvailableTimeSlotsUseCase.getAvailableTimeSlots("pos1", localDate))
//                .thenReturn(mockTimeSlots);
//        ResponseEntity<List<TimeSlotResponse>> response =
//                scheduleController.getAvailableTimeSlots("pos1", date);
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(2, response.getBody().size());
//        verify(getAvailableTimeSlotsUseCase).getAvailableTimeSlots("pos1", localDate);
//    }
//
//    @Test
//    void getAvailableTimeSlots_WithNoTimeSlots_ShouldReturnEmptyList() {
//        String date = "2024-01-15";
//        LocalDate localDate = LocalDate.parse(date);
//
//        when(getAvailableTimeSlotsUseCase.getAvailableTimeSlots("pos1", localDate))
//                .thenReturn(Collections.emptyList());
//        ResponseEntity<List<TimeSlotResponse>> response =
//                scheduleController.getAvailableTimeSlots("pos1", date);
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().isEmpty());
//        verify(getAvailableTimeSlotsUseCase).getAvailableTimeSlots("pos1", localDate);
//    }
//
//    @Test
//    void createOperatingHours_ShouldReturnOperatingHoursResponse() {
//        CreateOperatingHoursRequest request = new CreateOperatingHoursRequest();
//        request.setPointOfSaleId("pos1");
//        request.setDayOfWeek(DayOfWeek.MONDAY);
//        request.setOpeningTime(LocalTime.of(9, 0));
//        request.setClosingTime(LocalTime.of(17, 0));
//        OperatingHours mockOperatingHours = mock(OperatingHours.class);
//        when(manageOperatingHoursUseCase.createOperatingHours(
//                eq("pos1"), eq(DayOfWeek.MONDAY), any(LocalTime.class), any(LocalTime.class)))
//                .thenReturn(mockOperatingHours);
//        ResponseEntity<OperatingHoursResponse> response =
//                scheduleController.createOperatingHours(request, "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        verify(manageOperatingHoursUseCase).createOperatingHours(
//                eq("pos1"), eq(DayOfWeek.MONDAY), any(LocalTime.class), any(LocalTime.class));
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSale_ShouldReturnOperatingHoursList() {
//        OperatingHours opHours1 = mock(OperatingHours.class);
//        OperatingHours opHours2 = mock(OperatingHours.class);
//        List<OperatingHours> mockOperatingHours = List.of(opHours1, opHours2);
//        when(manageOperatingHoursUseCase.getOperatingHoursByPointOfSale("pos1"))
//                .thenReturn(mockOperatingHours);
//        ResponseEntity<List<OperatingHoursResponse>> response =
//                scheduleController.getOperatingHoursByPointOfSale("pos1", "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(2, response.getBody().size());
//        verify(manageOperatingHoursUseCase).getOperatingHoursByPointOfSale("pos1");
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSaleAndDay_ShouldReturnFilteredOperatingHours() {
//        String dayOfWeek = "monday";
//        OperatingHours opHours = mock(OperatingHours.class);
//        List<OperatingHours> mockOperatingHours = List.of(opHours);
//        when(manageOperatingHoursUseCase.getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.MONDAY)).thenReturn(mockOperatingHours);
//        ResponseEntity<List<OperatingHoursResponse>> response =
//                scheduleController.getOperatingHoursByPointOfSaleAndDay("pos1", dayOfWeek, "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().size());
//        verify(manageOperatingHoursUseCase).getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.MONDAY);
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSaleAndDay_WithUpperCaseDay_ShouldConvertToUpperCase() {
//        String dayOfWeek = "MONDAY";
//
//        OperatingHours opHours = mock(OperatingHours.class);
//        List<OperatingHours> mockOperatingHours = List.of(opHours);
//
//        when(manageOperatingHoursUseCase.getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.MONDAY))
//                .thenReturn(mockOperatingHours);
//
//        ResponseEntity<List<OperatingHoursResponse>> response =
//                scheduleController.getOperatingHoursByPointOfSaleAndDay("pos1", dayOfWeek, "user1");
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        verify(manageOperatingHoursUseCase).getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.MONDAY);
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSaleAndDay_WithNoResults_ShouldReturnEmptyList() {
//        String dayOfWeek = "tuesday";
//
//        when(manageOperatingHoursUseCase.getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.TUESDAY))
//                .thenReturn(Collections.emptyList());
//
//        ResponseEntity<List<OperatingHoursResponse>> response =
//                scheduleController.getOperatingHoursByPointOfSaleAndDay("pos1", dayOfWeek, "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().isEmpty());
//        verify(manageOperatingHoursUseCase).getOperatingHoursByPointOfSaleAndDay("pos1", DayOfWeek.TUESDAY);
//    }
//
//    @Test
//    void deleteOperatingHours_ShouldCallDeleteMethod() {
//        ResponseEntity<Void> response = scheduleController.deleteOperatingHours("op1", "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        verify(manageOperatingHoursUseCase).deleteOperatingHours("op1");
//    }
//
//    @Test
//    void createTemporaryClosure_ShouldReturnTemporaryClosureResponse() {
//        CreateTemporaryClosureRequest request = new CreateTemporaryClosureRequest();
//        request.setPointOfSaleId("pos1");
//        request.setStartDateTime(LocalDateTime.now());
//        request.setEndDateTime(LocalDateTime.now().plusDays(1));
//        request.setReason("Maintenance");
//        TemporaryClosure mockClosure = mock(TemporaryClosure.class);
//        when(manageTemporaryClosuresUseCase.createTemporaryClosure(
//                eq("pos1"), any(LocalDateTime.class), any(LocalDateTime.class), eq("Maintenance")))
//                .thenReturn(mockClosure);
//
//        ResponseEntity<TemporaryClosureResponse> response =
//                scheduleController.createTemporaryClosure(request, "user1");
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        verify(manageTemporaryClosuresUseCase).createTemporaryClosure(
//                eq("pos1"), any(LocalDateTime.class), any(LocalDateTime.class), eq("Maintenance"));
//    }
//
//    @Test
//    void getTemporaryClosuresByPointOfSale_ShouldReturnClosuresList() {
//        TemporaryClosure closure1 = mock(TemporaryClosure.class);
//        TemporaryClosure closure2 = mock(TemporaryClosure.class);
//        List<TemporaryClosure> mockClosures = List.of(closure1, closure2);
//
//        when(manageTemporaryClosuresUseCase.getClosuresByPointOfSale("pos1"))
//                .thenReturn(mockClosures);
//        ResponseEntity<List<TemporaryClosureResponse>> response =
//                scheduleController.getTemporaryClosuresByPointOfSale("pos1", "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(2, response.getBody().size());
//        verify(manageTemporaryClosuresUseCase).getClosuresByPointOfSale("pos1");
//    }
//
//    @Test
//    void getTemporaryClosuresByPointOfSale_WithNoClosures_ShouldReturnEmptyList() {
//        when(manageTemporaryClosuresUseCase.getClosuresByPointOfSale("pos1"))
//                .thenReturn(Collections.emptyList());
//        ResponseEntity<List<TemporaryClosureResponse>> response =
//                scheduleController.getTemporaryClosuresByPointOfSale("pos1", "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().isEmpty());
//        verify(manageTemporaryClosuresUseCase).getClosuresByPointOfSale("pos1");
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSale_WithNoResults_ShouldReturnEmptyList() {
//        when(manageOperatingHoursUseCase.getOperatingHoursByPointOfSale("pos1"))
//                .thenReturn(Collections.emptyList());
//        ResponseEntity<List<OperatingHoursResponse>> response =
//                scheduleController.getOperatingHoursByPointOfSale("pos1", "user1");
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().isEmpty());
//        verify(manageOperatingHoursUseCase).getOperatingHoursByPointOfSale("pos1");
//    }
//
//    @Test
//    void getOperatingHoursByPointOfSaleAndDay_WithInvalidDay_ShouldThrowException() {
//        String invalidDay = "INVALID_DAY";
//        assertThrows(IllegalArgumentException.class, () -> {
//            scheduleController.getOperatingHoursByPointOfSaleAndDay("pos1", invalidDay, "user1");
//        });
//    }
//
//    @Test
//    void checkAvailability_WithNullRequest_ShouldThrowException() {
//        assertThrows(NullPointerException.class, () -> {
//            scheduleController.checkAvailability(null);
//        });
//    }
//
//    @Test
//    void createOperatingHours_WithNullRequest_ShouldThrowException() {
//        assertThrows(NullPointerException.class, () -> {
//            scheduleController.createOperatingHours(null, "user1");
//        });
//    }
//
//    @Test
//    void createTemporaryClosure_WithNullRequest_ShouldThrowException() {
//        assertThrows(NullPointerException.class, () -> {
//            scheduleController.createTemporaryClosure(null, "user1");
//        });
//    }
//}