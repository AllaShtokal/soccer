package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "confirmation_reservation")
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

    @PrePersist
    @Transactional
    public void prePersist() {
        this.timeToSend = reservation.getDateFrom().plusHours(1);
    }
}
