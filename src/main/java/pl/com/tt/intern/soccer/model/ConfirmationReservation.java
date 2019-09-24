package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@Table(name = "confirmation_reservation")
@EqualsAndHashCode(exclude = {"reservation"})
public class ConfirmationReservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @NotNull
    @Column(name = "uuid", unique = true)
    private String uuid;

    @NotNull
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @NotNull
    @Column(name = "time_to_mail_send")
    private LocalDateTime timeToMailSend;

    @Column(name = "email_sent")
    private Boolean emailSent;

    public ConfirmationReservation(Reservation reservation) {
        this.reservation = reservation;
        this.uuid = UUID.randomUUID().toString();
        this.expirationTime = reservation.getDateFrom().minusMinutes(15);
        this.timeToMailSend = reservation.getDateFrom().minusHours(1);
        this.emailSent = false;
    }
}
