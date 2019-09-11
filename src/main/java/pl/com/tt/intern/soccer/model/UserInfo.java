package pl.com.tt.intern.soccer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.tt.intern.soccer.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo extends DateAudit {

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

    public UserInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
