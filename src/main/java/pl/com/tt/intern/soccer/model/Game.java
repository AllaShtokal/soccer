package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "game")
@Setter
@Getter
public class Game {

       @Id
       @GeneratedValue(strategy = IDENTITY)
       @Column(name = "game_id")
       private Long id;

       @Column(name = "is_active", nullable = false)
       private Boolean isActive;

       @ManyToOne
       @JoinColumn(name="match_id", nullable=false)
       private Match match;

       @OneToMany(mappedBy="game")
       private Set<Buttle>  buttles;

       public void addButtle(Buttle buttle) {
              this.buttles.add(buttle);
              buttle.setGame(this);

       }



}
