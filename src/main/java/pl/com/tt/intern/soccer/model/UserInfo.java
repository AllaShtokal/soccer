package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Column(name = "first_name")
    private String firstName;
    
    @Size(min = 3, max = 20)
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "skype")
    private String skype;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
