package pl.com.tt.intern.soccer.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {

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
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getType().toString()))
                .collect(toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
