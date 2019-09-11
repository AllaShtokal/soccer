package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.tt.intern.soccer.model.enums.ProcessType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @NotNull
    @Column(name = "uuid")
    private String uuid ;

    @NotNull
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;


    public Token(User user, ProcessType processType) {
        this.user = user;
        this.processType = processType;
        this.uuid = randomUUID().toString();
        this.expirationTime = LocalDateTime.now().plusHours(24);
    }
}
