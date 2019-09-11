package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;
import pl.com.tt.intern.soccer.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User extends DateAudit {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Username
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

    @Password
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

    @OneToOne(mappedBy = "user")
    private UserInfo userInfo;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(UserInfo userInfo, String username, String email, String password) {
        this.userInfo = userInfo;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
