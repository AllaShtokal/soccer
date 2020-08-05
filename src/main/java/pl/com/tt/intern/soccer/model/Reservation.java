package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "reservation")
@EqualsAndHashCode(exclude = "user")
public class Reservation implements Serializable {

    private static final long serialVersionUID = -6635819115881755604L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    @NotNull
    @Column(name = "date_from", nullable = false, unique = true)
    private LocalDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private LocalDateTime dateTo;

    @NotNull
    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

    @OneToMany(mappedBy = "reservation")
    Set<UserReservationEvent> userReservationEvents;

    @OneToMany(mappedBy="reservation")
    private Set<Match> matchSet;
}
