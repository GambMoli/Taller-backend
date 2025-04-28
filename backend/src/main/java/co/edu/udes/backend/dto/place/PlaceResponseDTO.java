package co.edu.udes.backend.dto.place;


import co.edu.udes.backend.models.Reserve;
import lombok.Data;

import java.util.List;

@Data
public class PlaceResponseDTO {
    private long id;
    private String type;
    private String name;
    private int quantity;
    private boolean available;
    private String description;
}
