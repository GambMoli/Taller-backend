package co.edu.udes.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


@Entity
@Data
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(name = "reserve_date", nullable = false)
    private LocalDate reserveDate ;

    @Column (name = "Code", nullable = false)
    private String code;
    @Column(name = "HourInit", nullable = false)
    private LocalTime hourInit;
    @Column(name = "HourFinish", nullable = false)
    private LocalTime hourFinish;
    @Column(name = "State" ,nullable = false)
    private String state;
    @ManyToOne
    @JoinColumn(name = "place_Id")
    private Place place;
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;
}
