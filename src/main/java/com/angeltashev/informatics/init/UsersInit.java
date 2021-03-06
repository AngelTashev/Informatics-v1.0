package com.angeltashev.informatics.init;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UsersInit implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final AuthorityProcessingService authorityProcessingService;

    private final UserService userService;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (this.authorityRepository.count() == 0) {
            this.authorityProcessingService.seedAuthorities();
        }
        if (this.userRepository.count() == 0) {
            // Root admin
            UserEntity root = new UserEntity();
            root.setFullName("Root Admin");
            root.setUsername("root");
            root.setEmail("root@root.toor");
            root.setPassword(passwordEncoder.encode("root123"));
            root.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_ROOT_ADMIN"));
            root.setGrade(12);
            root.setGradeClass("ZH");
            root.setRegistrationDate(LocalDateTime.now());
            root.setActive(true);
            root.setPoints(0);
            this.userRepository.saveAndFlush(root);

            // Admin
            UserEntity admin = new UserEntity();
            admin.setFullName("Admin");
            admin.setUsername("admin");
            admin.setEmail("admin@admin.adm");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
            admin.setGrade(11);
            admin.setGradeClass("V");
            admin.setRegistrationDate(LocalDateTime.now());
            admin.setActive(true);
            admin.setPoints(0);
            this.userRepository.saveAndFlush(admin);

            // Pesho
            UserEntity pesho = new UserEntity();
            pesho.setFullName("Pesho Ivanov");
            pesho.setUsername("pesho_ivanov.2020");
            pesho.setEmail("pesho@gmail.com");
            pesho.setPassword(passwordEncoder.encode("pesho123"));
            pesho.getAuthorities().add(this.authorityRepository.findByAuthority("ROLE_STUDENT"));
            pesho.setGrade(9);
            pesho.setGradeClass("A");
            pesho.setPhrase("I'm a student!");
            pesho.setRegistrationDate(LocalDateTime.now());
            pesho.setActive(true);
            pesho.setPoints(0);
            this.userRepository.saveAndFlush(pesho);
        }
        if (this.assignmentRepository.count() == 0) {
            AssignmentEntity springAssignment = new AssignmentEntity();
            springAssignment.setName("Spring Database");
            springAssignment.setDescription("Create a basic database with Spring MVC");
            springAssignment.setUser(this.userRepository.findByUsername("pesho_ivanov.2020").orElse(null));
            springAssignment.setDueDate(LocalDateTime.of(2020, 8, 5, 12, 0));
            springAssignment.setEnabled(true);
            this.assignmentRepository.saveAndFlush(springAssignment);
        }
    }
}
