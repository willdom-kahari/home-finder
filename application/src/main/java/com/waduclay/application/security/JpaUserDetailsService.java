package com.waduclay.application.security;

import com.waduclay.homefinder.ports.UserQuery;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserQuery userQuery;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userQuery.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username and password"));
        return new SecurityUser(user);
    }
}
