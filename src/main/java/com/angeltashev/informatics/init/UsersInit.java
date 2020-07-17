package com.angeltashev.informatics.init;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UsersInit implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final AuthorityService authorityService;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        if (this.authorityRepository.count() == 0) {
            this.authorityService.seedAuthorities();
        }
        if (this.userRepository.count() == 0) {
            // Root admin
            UserEntity root = new UserEntity();
            root.setUsername("root");
            root.setEmail("root@root.toor");
            root.setPassword(passwordEncoder.encode("root123"));
            root.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_ROOT_ADMIN"));
            root.setRegistrationDate(LocalDateTime.now());
            root.setActive(true);
            this.userRepository.saveAndFlush(root);

            // Admin
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.adm");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
            admin.setRegistrationDate(LocalDateTime.now());
            admin.setActive(true);
            this.userRepository.saveAndFlush(admin);

            // Pesho
            UserEntity pesho = new UserEntity();
            pesho.setUsername("pesho");
            pesho.setEmail("pesho@gmail.com");
            pesho.setPassword(passwordEncoder.encode("peshos123"));
            pesho.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_USER"));
            pesho.setRegistrationDate(LocalDateTime.now());
            pesho.setActive(true);
            this.userRepository.saveAndFlush(pesho);
        }
    }
}
