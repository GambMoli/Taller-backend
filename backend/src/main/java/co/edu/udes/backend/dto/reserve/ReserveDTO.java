package co.edu.udes.backend.dto.reserve;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.time.LocalTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
    private String code;
    private LocalDate reserveDate;
    private LocalTime hourInit;
    private LocalTime hourFinish;
    private String state;
    private long placeId;
    private long teacherId;
    private long studentId;
}
