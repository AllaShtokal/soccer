package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "process_type")
    private ProcessType processType;

    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "expired_date_time")
    private LocalDateTime expiredDateTime = LocalDateTime.now().plusHours(24);


    public Token(User user, @NotNull ProcessType processType) {
        this.user = user;
        this.processType = processType;
    }
}
