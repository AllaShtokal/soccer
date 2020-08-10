package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "matchh")
@Getter
@Setter
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private LocalDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = true)
    private LocalDateTime dateTo;

    @OneToMany(mappedBy="matchm", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive ;

    @OneToMany(mappedBy="match", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
        team.setMatchm(this);

    }



}
