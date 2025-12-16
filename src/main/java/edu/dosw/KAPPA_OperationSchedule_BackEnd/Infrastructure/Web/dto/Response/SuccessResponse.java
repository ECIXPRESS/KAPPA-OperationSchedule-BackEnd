package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse<T> {
    private LocalDateTime timestamp;
    private String message;
    private T data;

    public static <T> SuccessResponse<T> create(String message, T data) {
        return new SuccessResponse<>(LocalDateTime.now(), message, data);
    }
}