package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Role;
import pl.com.tt.intern.soccer.model.enums.RoleType;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByType(RoleType roleName);

}
