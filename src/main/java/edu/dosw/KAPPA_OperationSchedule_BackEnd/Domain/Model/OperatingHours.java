package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.DayOfWeek;
import java.time.LocalTime;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Utils.IdGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "operating_hours")
public class OperatingHours {
    @Id
    private String id;
    private String pointOfSaleId;
    private DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean active = true;

    public OperatingHours(String pointOfSaleId, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        this.pointOfSaleId = pointOfSaleId;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}