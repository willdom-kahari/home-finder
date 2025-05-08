package com.waduclay.application.security;

import com.waduclay.application.users.UserQueryAdapter;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserQueryAdapter userQuery;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userQuery.findAuthUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username and password"));
        return new SecurityUser(user);
    }
}
