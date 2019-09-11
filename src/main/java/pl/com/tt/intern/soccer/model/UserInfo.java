package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 20)
    @NotBlank
    @Column(name = "first_name",
            nullable = false,
            length = 20)
    private String firstName;

    @Size(min = 3, max = 20)
    @NotBlank
    @Column(name = "last_name",
            nullable = false,
            length = 20)
    private String lastName;

    @Column(name = "phone",
            length = 12)
    private String phone;

    @Column(name = "skype",
            length = 30)
    private String skype;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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
