package pl.com.tt.intern.soccer.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

    UserDetails loadUserById(Long id);
}
