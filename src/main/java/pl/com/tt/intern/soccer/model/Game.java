package pl.com.tt.intern.soccer.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "game")

public class Game implements Serializable {
       private static final long serialVersionUID = 9114226883488912345L;

       @Id
       @GeneratedValue(strategy = IDENTITY)
       @Column(name = "id")
       private Long id;

       @Column(name = "is_active", nullable = false)
       private Boolean isActive;

       @ManyToOne
       @JoinColumn(name="matchh_id", nullable=false)
       private Match matchh;

       @OneToMany(mappedBy="game", cascade = CascadeType.ALL)
       private Set<Buttle>  buttles = new HashSet<>();

       public void addButtle(Buttle buttle) {
              this.buttles.add(buttle);
              buttle.setGame(this);

       }



}
