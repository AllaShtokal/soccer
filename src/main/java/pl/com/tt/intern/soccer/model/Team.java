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
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 9114228993488956491L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "team_name",
            nullable = false,
            length = 20)
    private String name;

    @Column(name = "active",
            nullable = false)
    private Boolean active;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matchh_id", nullable = false)
    private Match matchm;

    @ManyToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.getTeam().add(this);

    }


}
