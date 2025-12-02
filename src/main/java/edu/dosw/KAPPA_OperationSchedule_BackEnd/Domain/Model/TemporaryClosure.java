package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Utils.IdGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "temporary_closures")
public class TemporaryClosure {
    @Id
    private String id;
    private String pointOfSaleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String reason;
    private TemporaryClosureType closureType = TemporaryClosureType.MAINTENANCE;
    private Boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    public TemporaryClosure(String pointOfSaleId, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        this.pointOfSaleId = pointOfSaleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reason = reason;

    }
}