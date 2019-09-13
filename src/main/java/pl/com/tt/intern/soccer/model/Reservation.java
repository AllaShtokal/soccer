package pl.com.tt.intern.soccer.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @NotNull
    @Column(name="date", nullable = false, unique = true)
    private LocalDateTime date;

    @NotNull
    @Column(name="period", nullable = false)
    private LocalDateTime period;

    @NotNull
    @Column(name="confirmed", nullable = false)
    private Boolean confirmed;
}
