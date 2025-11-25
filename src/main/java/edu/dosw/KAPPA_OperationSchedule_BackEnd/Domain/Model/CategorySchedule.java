package edu.dosw.KAPPA_OperationSchedule_BackEnd.Domain.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;

@Document(collection = "category_schedules")
public class CategorySchedule {
    @Id
    private String id;
    private String categoryName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active = true;

    public CategorySchedule() {}

    public CategorySchedule(String categoryName, LocalTime startTime, LocalTime endTime) {
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}