package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "day")
    private List<String> days;

    @Temporal(TemporalType.TIME)
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    public Schedule(List<String> days, Date startTime, Date endTime) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Schedule() {}

    public void createSchedule(List<String> days, Date startTime, Date endTime) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void editSchedule(List<String> days, Date startTime, Date endTime) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
