package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Role;
import pl.com.tt.intern.soccer.model.enums.RoleType;
import pl.com.tt.intern.soccer.repository.RoleRepository;
import pl.com.tt.intern.soccer.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByType(RoleType type) throws NotFoundException {
        return roleRepository.findByType(type)
                .orElseThrow(NotFoundException::new);
    }
}
