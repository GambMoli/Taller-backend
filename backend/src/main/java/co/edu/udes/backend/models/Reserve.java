package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserved_by", nullable = false)
    private User reservedBy;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status")
    private String status;  // Could be "pending", "confirmed", etc.

    public void createReserve(User user, LocalDateTime date) {
        this.reservedBy = user;
        this.date = date;
        this.status = "pending";
    }

    public void enableReserve() {
        this.status = "confirmed";
    }
}
