package pl.com.tt.intern.soccer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "lobby")
public class Lobby implements Serializable {

    private static final long serialVersionUID = -6635819115881755601L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lobby_name",
            unique = true,
            nullable = false,
            length = 20)
    private String name;

    @Column(name = "available",
            nullable = false)
    private Boolean available;

    @Column(name = "limit_members",
            nullable = false)
    private  Long limit;

    @OneToMany(mappedBy = "lobby", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Reservation> reservations;

}
