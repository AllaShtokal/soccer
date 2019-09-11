package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "process_type")
    private String processType;

    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "expired_date_time")
    private LocalDateTime expiredDateTime = LocalDateTime.now().plusHours(24);
}
