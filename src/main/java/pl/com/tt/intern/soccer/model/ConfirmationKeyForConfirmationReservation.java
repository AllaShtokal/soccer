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
@Table(name = "confirmation_key_for_confirmation_reservation")
@EqualsAndHashCode(exclude = "reservation")
public class ConfirmationKeyForConfirmationReservation {

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
    private String uuid ;

    @NotNull
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    public ConfirmationKeyForConfirmationReservation(Reservation reservation) {
        this.reservation = reservation;
        this.uuid = UUID.randomUUID().toString();
        this.expirationTime = reservation.getDateFrom().minusMinutes(1);
    }
}
