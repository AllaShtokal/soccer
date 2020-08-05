package pl.com.tt.intern.soccer.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "lobby")
public class Lobby implements Serializable {

    private static final long serialVersionUID = -6635819115881755601L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lobby_id")
    private Long id;


    @Column(name = "lobby_name",
            unique = true,
            nullable = false,
            length = 20)
    private String name;

    @Column(name = "available",
            nullable = false)
    private Boolean available;

    @Column(name = "limit",
            nullable = false)
    private  Long limit;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Reservation> reservations;

}
