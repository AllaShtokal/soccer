package pl.com.tt.intern.soccer.model;

import lombok.Data;
import pl.com.tt.intern.soccer.model.enums.RoleType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "type")
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

}
