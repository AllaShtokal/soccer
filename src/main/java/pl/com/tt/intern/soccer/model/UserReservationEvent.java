package pl.com.tt.intern.soccer.model;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user_reservation")
public class UserReservationEvent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    @Column(name = "registered_at", nullable = false)
    LocalDateTime registeredAt;




}
