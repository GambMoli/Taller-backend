package co.edu.udes.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("Material")
public class Material extends Resource {

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public void borrowMaterial() {
        if (this.quantity > 0) {
            this.quantity--;
            this.setState("unavailable");
        } else {
            throw new RuntimeException("No materials available to borrow.");
        }
    }

    public void returnMaterial() {
        this.quantity++;
        if (this.quantity > 0) {
            this.setState("available");
        }
    }

    public boolean checkAvailability() {
        return this.quantity > 0;
    }
}
