package pl.com.tt.intern.soccer.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user_reservation")
public class UserReservationEvent implements Serializable {

    private static final long serialVersionUID = 911422688348896791L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private  Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;




}
