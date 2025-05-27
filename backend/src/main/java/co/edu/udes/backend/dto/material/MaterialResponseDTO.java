package co.edu.udes.backend.dto.material;


import lombok.Data;

import java.time.LocalDate;

@Data
public class MaterialResponseDTO {
    private long id;
    private String name;
    private String description;
    private String type;
    private String state;
    private int stock;
    private LocalDate entryDate;

}
