package pl.com.tt.intern.soccer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "reservation")
@EqualsAndHashCode(exclude = "user")
public class Reservation implements Serializable {

    private static final long serialVersionUID = -6635819115881755604L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "date_from", nullable = false, unique = true)
    private LocalDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private LocalDateTime dateTo;

    @NotNull
    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

}
