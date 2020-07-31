package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.UserHomeViewModel;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserRoleViewModel;
import com.angeltashev.informatics.user.model.view.UserVisitViewModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthorityProcessingService authorityProcessingService;

    private final UserRepository userRepository;

    @Override
    public boolean registerUser(UserRegisterBindingModel registerModel) {
        UserEntity user = this.modelMapper.map(registerModel, UserEntity.class);
        user.setPassword(this.passwordEncoder.encode(registerModel.getPassword()));
        user.setAuthorities(Set.of(authorityProcessingService.getStudentAuthority()));
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        user.setPoints(0);
        this.userRepository.saveAndFlush(user);
        log.info("Register user: Registered user " + user.getFullName() + " with username: " + user.getUsername());
        return true;
    }


    // TODO Log after refactoring
    @Override
    public UserProfileViewModel getUserProfile(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(new UserEntity());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(
                userEntity,
                UserProfileViewModel.class
        );
        userProfileViewModel.setAuthority(getUserAuthority(userEntity));
        byte[] profilePicture = userEntity.getProfilePicture();
        String profilePictureString = "";
        if (profilePicture != null) {
            profilePictureString = Base64.getEncoder().encodeToString(userEntity.getProfilePicture());
        }
        userProfileViewModel.setProfilePictureString(profilePictureString);
        return userProfileViewModel;
    }

    @Override
    public UserVisitViewModel getUserVisitProfile(String username) throws PageNotFoundException {

        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
        if (userEntity == null) {
            log.error("Get user visit profile: Username is not found");
            throw new PageNotFoundException("Username is not found");
        }
        UserVisitViewModel userVisitViewModel = this.modelMapper.map(
                userEntity,
                UserVisitViewModel.class
        );
        userVisitViewModel.setAuthority(getUserAuthority(userEntity));
        byte[] profilePicture = userEntity.getProfilePicture(); //TODO Refactor
        String profilePictureString = "";
        if (profilePicture != null) {
            profilePictureString = Base64.getEncoder().encodeToString(userEntity.getProfilePicture());
        }
        userVisitViewModel.setProfilePictureString(profilePictureString);
        log.info("Get user visit profile: Retrieved user view model with username: " + username);
        return userVisitViewModel;
    }

    @Override
    public UserHomeViewModel getUserHomeDetails(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(new UserEntity());
        UserHomeViewModel userHomeViewModel = this.modelMapper.map(
                userEntity,
                UserHomeViewModel.class
        );
        log.info("Get user home details: Retrieved user view model with username: " + username);
        return userHomeViewModel;
    }

    @Override
    public UserDTO findByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElse(null);
//        if (user == null)
//            log.info("Find by username: user with this username does not exist");
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElse(null);
//        if (user == null)
//            log.info("Find by email: user with this email does not exist");
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public List<UserAssignmentAddBindingModel> getUserAssignmentAddModels() {
        log.info("Get user assignment add models: Retrieving all users");
        return this.getAllUsers()
                .stream()
                .filter(user -> !user.getUsername().equals("root"))
                .map(user -> this.modelMapper.map(user, UserAssignmentAddBindingModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void uploadPicture(String username, MultipartFile file) throws FileStorageException {

        // TODO Refactor and log
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("An error occured."));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid characters!");
            }

            user.setProfilePicture(file.getBytes());
            this.userRepository.save(user);
        } catch (IOException e) {
            throw new FileStorageException("Could not upload picture " + fileName + ". Please try again!", e);
        }
    }

    private String getUserAuthority(UserEntity user) {
        List<String> authorities = user.getAuthorities().stream()
                .map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList());
        if (authorities.contains("ROLE_ADMIN")) return "teacher";
        return "student";
    }

    @Override
    public UserDTO getUserDTO(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Get user DTO: Username cannot be found!" + username);
            throw new UsernameNotFoundException("Username cannot be found");
        });
        log.info("Get user DTO: Retrieved user DTO with username: " + username);
        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public boolean addPointsToUser(String username, Integer points) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
        Integer currentPoints = userEntity.getPoints();
        userEntity.setPoints(currentPoints + points);
        log.info("Add points to user: Added " + points + " points to " + username);
        this.userRepository.save(userEntity);
        return true;
    }

    @CachePut("users")
    @Override
    public List<UserEntity> updateAllStudents() {
        log.info("Update all students: Updating users cache!");
        return getAllUsers();
    }

    @Override
    public List<UserRoleViewModel> getAllAdmins() {
        return this.getAllUsersByAuthority("ROLE_ADMIN");
    }

    @Override
    public List<UserRoleViewModel> getAllStudents() {
        return this.getAllUsersByAuthority("ROLE_STUDENT");
    }

    @Override
    public boolean demoteAdminById(String adminId) {
        UserEntity admin = this.userRepository.findById(adminId).orElse(null);
        if (admin == null) {
            log.error("Demote admin by id: Admin with id " + adminId + " cannot be found");
            throw new UsernameNotFoundException("Admin with id " + adminId + " cannot be found!");
        }
        admin.setAuthorities(Set.of(this.authorityProcessingService.getStudentAuthority()));
        this.userRepository.save(admin);
        log.info("Demote admin by id: Admin with id " + adminId + " demoted to student successfully");
        return true;
    }

    @Override
    public boolean promoteStudentById(String studentId) {

        UserEntity student = this.userRepository.findById(studentId).orElse(null);
        if (student == null) {
            log.error("Promote student by id: Student with id " + studentId + " cannot be found");
            throw new UsernameNotFoundException("Student with id " + studentId + " cannot be found!");
        }
        student.setAuthorities(Set.of(this.authorityProcessingService.getAdminAuthority()));
        this.userRepository.save(student);
        log.info("Promote student by id: Student with id " + studentId + " promoted to admin successfully");
        return true;
    }

    private List<UserRoleViewModel> getAllUsersByAuthority(String authority) {
        List<UserEntity> users = this.userRepository.findAll()
                .stream()
                .filter(user -> {
                    Set<String> authorities = user.getAuthorities()
                            .stream()
                            .map(AuthorityEntity::getAuthority)
                            .collect(Collectors.toSet());
                    if (authorities.contains(authority)) return true;
                    return false;
                })
                .collect(Collectors.toList());
        return users.stream()
                .map(user -> this.modelMapper.map(user, UserRoleViewModel.class))
                .collect(Collectors.toList());
    }

    @Cacheable("users")
    public List<UserEntity> getAllUsers() {
        log.info("Get all students: Retrieving all cached users");
        return this.userRepository.findAll();
    }
}
