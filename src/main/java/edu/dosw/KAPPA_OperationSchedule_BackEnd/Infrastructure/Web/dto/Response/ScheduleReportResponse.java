package edu.dosw.KAPPA_OperationSchedule_BackEnd.Infrastructure.Web.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleReportResponse {
    private String reportType;
    private LocalDateTime generatedAt;
    private Map<String, Object> data;

    public static ScheduleReportResponse create(String reportType, Map<String, Object> data) {
        return new ScheduleReportResponse(reportType, LocalDateTime.now(), data);
    }
}