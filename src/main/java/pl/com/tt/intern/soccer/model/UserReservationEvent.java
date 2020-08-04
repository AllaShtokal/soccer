package pl.com.tt.intern.soccer.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_reservation")
public class UserReservationEvent {


    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    @Column(name = "registered_at",
            nullable = false)
    LocalDateTime registeredAt;
}
