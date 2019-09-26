package pl.com.tt.intern.soccer.model;

import lombok.Data;
import pl.com.tt.intern.soccer.model.enums.RoleType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 336446670402248107L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "type")
    private RoleType type;

}
