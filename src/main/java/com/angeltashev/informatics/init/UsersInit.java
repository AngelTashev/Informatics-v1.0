package com.angeltashev.informatics.init;

import com.angeltashev.informatics.user.model.RoleEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UsersInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (this.userRepository.count() == 0) {
            UserEntity user = new UserEntity();
            RoleEntity role = new RoleEntity();
            role.setRole("ROLE_USER");
            user.setUsername("ivan");
            user.setEmail("ivan@abv.com");
            user.setPasswordHash(passwordEncoder.encode("123"));
            user.getRoles().add(role);
            this.userRepository.saveAndFlush(user);
        }
    }
}
