package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "matchh")
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private LocalDateTime dateFrom;

    @Column(name = "date_to", nullable = true)
    private LocalDateTime dateTo;

    @OneToMany(mappedBy="matchm", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Team> teams = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive ;

    @OneToMany(mappedBy="matchh", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Game> games = new HashSet<>();;

    @ManyToOne
    @JoinColumn(name="reservation_id", nullable=false)
    private Reservation reservation;

    public void addGame(Game game) {
        this.games.add(game);
        game.setMatchh(this);

    }
    public void addTeam(Team team) {
        this.teams.add(team);
        team.setMatchm(this);

    }



}
