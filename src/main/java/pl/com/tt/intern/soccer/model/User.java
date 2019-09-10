package pl.com.tt.intern.soccer.model;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private Long id;

    @Username
    @Column(unique = true)
    private String username;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Password
    @Column
    private String password;

    @Column
    private boolean locked;

    @Column
    private boolean enabled;

}
