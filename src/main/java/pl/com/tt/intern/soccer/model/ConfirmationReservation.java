package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "confirmation_reservation")
@NoArgsConstructor
@ToString
public class ConfirmationReservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation")
    private Reservation reservation;

    @Column(name = "emailSent")
    private boolean emailSent;

    @Column(name = "time_to_send")
    private LocalDateTime timeToSend;

    public ConfirmationReservation(Reservation reservation) {
        this.reservation = reservation;
        this.emailSent = false;
        this.timeToSend = reservation.getDateFrom().minusMinutes(1);
    }
}
