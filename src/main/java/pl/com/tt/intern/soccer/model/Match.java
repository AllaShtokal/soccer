package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.internal.Pair;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "match")
@Getter
@Setter
public class Match {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @NotNull
    @Column(name = "date_from", nullable = false, unique = true)
    private LocalDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = true)
    private LocalDateTime dateTo;

    @OneToMany(mappedBy="match", fetch = FetchType.EAGER)
    private Set<Team> teams;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive ;

    @OneToMany(mappedBy="match", fetch = FetchType.EAGER)
    private Set<Game> games;

    @ManyToOne
    @JoinColumn(name="reservation_id", nullable=false)
    private Reservation reservation;

    public void addGame(Game game) {
        this.games.add(game);
        game.setMatch(this);

    }
    public void addTeam(Team team) {
        this.teams.add(team);
        team.setMatch(this);

    }



}
