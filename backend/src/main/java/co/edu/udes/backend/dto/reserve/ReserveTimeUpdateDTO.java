package co.edu.udes.backend.dto.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveTimeUpdateDTO {
    private LocalDate reserveDate;
    private LocalTime hourInit;
    private LocalTime hourFinish;
}
