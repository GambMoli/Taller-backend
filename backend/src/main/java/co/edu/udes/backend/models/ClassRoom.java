package co.edu.udes.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ClassRoom")
public class ClassRoom extends Resource {

    public void returnResource() {
        this.setState("available");
    }
}
