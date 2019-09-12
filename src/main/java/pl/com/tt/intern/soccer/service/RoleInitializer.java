package pl.com.tt.intern.soccer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.com.tt.intern.soccer.model.Role;
import pl.com.tt.intern.soccer.model.enums.RoleType;
import pl.com.tt.intern.soccer.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.Optional;

import static pl.com.tt.intern.soccer.model.enums.RoleType.ROLE_ADMIN;
import static pl.com.tt.intern.soccer.model.enums.RoleType.ROLE_USER;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        createRoleIfNotFound(ROLE_USER);
        createRoleIfNotFound(ROLE_ADMIN);
    }

    @Transactional
    void createRoleIfNotFound(RoleType type) {
        Optional<Role> role = roleRepository.findByType(type);
        Role newRole = new Role();
        if (role.isEmpty()) {
            newRole.setType(type);
            roleRepository.save(newRole);
        }
    }
}
