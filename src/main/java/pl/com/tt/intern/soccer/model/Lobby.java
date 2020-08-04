package pl.com.tt.intern.soccer.model;


import lombok.Data;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lobby_id")
    private Long id;


    @Column(name = "lobby_name",
            unique = true,
            nullable = false,
            length = 20)
    private String name;

    private boolean available;

    private  Long limit;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Reservation> reservations;

}
