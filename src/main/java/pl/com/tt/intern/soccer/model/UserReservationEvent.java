package pl.com.tt.intern.soccer.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "user_reservation")
public class UserReservationEvent {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_reservation_id")
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

    @OneToMany(mappedBy="user_reservation")
    private Set<Match> matchSet;
}
