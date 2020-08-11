package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;


@RequiredArgsConstructor
@Entity
@Table(name = "buttle")
@Getter
@Setter
public class Buttle {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 20)
    @Column(name = "first_team_name",
            nullable = false,
            length = 20)
    private String teamName1;

    @Size(min = 3, max = 20)
    @Column(name = "second_team_name",
            nullable = false,
            length = 20)
    private String teamName2;

    @Column(name = "score_team1",
            nullable = false)
    private int scoreTeam1;

    @Column(name = "score_team2",
            nullable = false)
    private int scoreTeam2;

    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game game;

    public Buttle(String teamName1,String teamName2) {
    }



}
