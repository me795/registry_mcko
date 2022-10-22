package ru.mcko.registry.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mcko.registry.entity.Role;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.repository.read.UserRepositoryRead;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryRead userRepositoryRead;

    public UserDetailsServiceImpl(UserRepositoryRead userRepositoryRead) {
        this.userRepositoryRead = userRepositoryRead;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryRead.findActiveByName(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user != null) {
            for (Role role : user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
        }else{
            return new org.springframework.security.core.userdetails.User(null, null, grantedAuthorities);
        }
    }
}
