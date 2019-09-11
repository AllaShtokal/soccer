package pl.com.tt.intern.soccer.model;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "user")
public class User {

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

    @OneToOne(mappedBy = "user")
    private Token token;

    @Column(name = "created_at")
    private LocalDateTime created_At;

    @Column(name = "updated_at")
    private LocalDateTime updated_At;

    @PrePersist
    private void prePersist() {
        this.created_At = now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updated_At = now();
    }

}
