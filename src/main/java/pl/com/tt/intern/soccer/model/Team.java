package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_id")
    private  Long team_id;

    @Column(name = "team_name",
            unique = true,
            nullable = false,
            length = 20)
    private String name;

    @Column(name = "active",
            nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name="match_id", nullable=false)
    private Match match;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private Set<User> users;

    public void addUser(User user) {
        this.users.add(user);
        user.setTeam(this);

    }


}
