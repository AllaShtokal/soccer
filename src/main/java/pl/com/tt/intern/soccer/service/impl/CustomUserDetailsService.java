package pl.com.tt.intern.soccer.service.impl;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

    UserDetails loadUserById(Long id);
}
