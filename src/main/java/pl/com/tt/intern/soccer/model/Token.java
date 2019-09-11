package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.tt.intern.soccer.model.enums.ProcessType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @Column(name = "uuid")
    private String uuid ;

    @Column(name = "expired_date_time")
    private LocalDateTime expiredDateTime;


    public Token(User user, @NotNull ProcessType processType) {
        this.user = user;
        this.processType = processType;
        this.uuid = UUID.randomUUID().toString();
        this.expiredDateTime = LocalDateTime.now().plusHours(24);
    }
}
