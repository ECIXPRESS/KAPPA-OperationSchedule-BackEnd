package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;
import edu.dosw.KAPPA_OperationSchedule_BackEnd.Utils.IdGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Setter
@Document(collection = "category_schedules")
@NoArgsConstructor
@AllArgsConstructor
public class CategorySchedule {
    @Id
    private String id;
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active = true;

    public CategorySchedule(String categoryName, LocalTime startTime, LocalTime endTime) {
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}