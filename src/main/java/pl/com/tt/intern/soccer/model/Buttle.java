package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "buttle")
@Getter
@Setter
public class Buttle {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Size(min = 3, max = 20)
    @NotBlank
    @Column(name = "first_name",
            nullable = false,
            length = 20)
    private String teamName1;

    @Size(min = 3, max = 20)
    @NotBlank
    @Column(name = "first_name",
            nullable = false,
            length = 20)
    private String teamName2;

    @Column(name = "scoreTeam1",
            nullable = false)
    private int scoreTeam1 = 0;

    @Column(name = "scoreTeam2",
            nullable = false)
    private int scoreTeam2 = 0;

    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game game;

    public Buttle(String teamName1,String teamName2) {
    }

}
