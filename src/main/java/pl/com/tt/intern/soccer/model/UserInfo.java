package pl.com.tt.intern.soccer.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pl.com.tt.intern.soccer.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
@EqualsAndHashCode(callSuper = true, exclude = "user")
public class UserInfo extends DateAudit implements Serializable {

    private static final long serialVersionUID = -4256607112910571292L;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "won")
    private Long won = 0L;

    @Column(name = "lost")
    @ColumnDefault("0")
    private Long lost = 0L;


    public UserInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addWon() {
        this.won++;
    }

    public void addLost() {
        this.lost++;
    }

}
