package pl.com.tt.intern.soccer.model;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Username
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Email
    @Size(max = 60)
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Password
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @OneToOne(mappedBy = "user")
    private UserInfo userInfo;

}
