package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "game")

public class Game {

       @Id
       @GeneratedValue(strategy = IDENTITY)
       @Column(name = "game_id")
       private Long id;

       @ManyToOne
       @JoinColumn(name="match_id", nullable=false)
       private Match match;

       @OneToMany(mappedBy="game")
       private Set<Buttle>  listOfButtles;


//       //previous winners
//       Set<Team> tempListofTeams;


       //create a game from list of Teams and list of Buttles








}
