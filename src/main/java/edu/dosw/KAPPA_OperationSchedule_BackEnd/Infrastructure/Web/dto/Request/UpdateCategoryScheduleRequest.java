package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryScheduleRequest {
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;
}