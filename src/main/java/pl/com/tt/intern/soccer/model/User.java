package pl.com.tt.intern.soccer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.com.tt.intern.soccer.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public class User extends DateAudit implements Serializable {

    private static final long serialVersionUID = -1674802143717599061L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username",
            unique = true,
            nullable = false,
            length = 20)
    private String username;

    @Email
    @Size(max = 70)
    @NotBlank
    @Column(name = "email",
            unique = true,
            nullable = false,
            length = 70)
    private String email;

    @Column(name = "password",
            nullable = false,
            length = 100)
    private String password;

    @Column(name = "locked",
            nullable = false)
    private boolean locked;

    @Column(name = "enabled",
            nullable = false)
    private boolean enabled;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = LAZY)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservation = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ConfirmationKey> confirmationKeys = new HashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<UserReservationEvent> userReservationEvents = new HashSet<>();

    public void addUserReservationEvent(UserReservationEvent userReservationEvent) {
        this.userReservationEvents.add(userReservationEvent);
        userReservationEvent.setUser(this);

    }

    public void removeUserReservationEvent(UserReservationEvent userReservationEvent) {
        this.userReservationEvents.remove(userReservationEvent);
        userReservationEvent.setUser(null);

    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_team",
    joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "team_id") })
    private Set<Team> team = new HashSet<>();

    public void addTeam(Team team) {
        this.team.add(team);
        team.getUsers().add(this);

    }


}
