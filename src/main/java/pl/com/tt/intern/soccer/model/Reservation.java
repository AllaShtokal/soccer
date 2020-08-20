package pl.com.tt.intern.soccer.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "reservation")
@EqualsAndHashCode(exclude = "user")
public class Reservation implements Serializable {

    private static final long serialVersionUID = -6635819115881755604L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<UserReservationEvent> userReservationEvents;

    @OneToMany(mappedBy="reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Match> matches = new HashSet<>();

    public void addUserReservationEvent(UserReservationEvent userReservationEvent) {
        this.userReservationEvents.add(userReservationEvent);
        userReservationEvent.setReservation(this);
    }
    public void removeUserReservationEvent(UserReservationEvent userReservationEvent) {
        this.userReservationEvents.remove(userReservationEvent);
        userReservationEvent.setReservation(null);
    }

    public void addMatch(Match match) {
        this.matches.add(match);
        match.setReservation(this);

    }



}
