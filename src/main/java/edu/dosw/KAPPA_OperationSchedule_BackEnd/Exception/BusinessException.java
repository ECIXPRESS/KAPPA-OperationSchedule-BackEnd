package edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public static BusinessException scheduleNotFound(String scheduleId) {
        String message = String.format("Horario de operación con ID %s no encontrado", scheduleId);
        return new BusinessException(message, "SCHEDULE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public static BusinessException pointOfSaleNotFound(String pointOfSaleId) {
        String message = String.format("Punto de venta con ID %s no encontrado", pointOfSaleId);
        return new BusinessException(message, "POINT_OF_SALE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public static BusinessException scheduleConflict(String message) {
        return new BusinessException(message, "SCHEDULE_CONFLICT", HttpStatus.CONFLICT);
    }

    public static BusinessException validationError(String message) {
        return new BusinessException(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() { return errorCode; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}