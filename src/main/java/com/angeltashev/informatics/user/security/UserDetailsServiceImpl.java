package com.angeltashev.informatics.user.security;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);
        return user.map(this::map).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    private UserDetails map(UserEntity user) {
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream().map(this::map).collect(Collectors.toSet())
        );
    }

    private GrantedAuthority map(AuthorityEntity role) {
        return new SimpleGrantedAuthority(role.getAuthority());
    }
}
