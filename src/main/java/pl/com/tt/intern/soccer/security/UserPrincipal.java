package pl.com.tt.intern.soccer.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@Builder
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = -3338834619238915184L;

    private Long id;
    private UserInfo userInfo;
    private String username;
    private boolean locked;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    public static UserPrincipal create(User user) {
        log.debug("Start getting user roles and save to simple granted authority..");
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getType().name())
        ).collect(Collectors.toList());

        log.debug("Creating new UserPrincipal..");
        return UserPrincipal.builder()
                .id(user.getId())
                .userInfo(user.getUserInfo())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .locked(user.isLocked())
                .enabled(user.isEnabled())
                .authorities(authorities)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
