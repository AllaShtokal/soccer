package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Role;
import pl.com.tt.intern.soccer.model.enums.RoleType;

public interface RoleService {

    Role findByType(RoleType type) throws NotFoundException;

}
