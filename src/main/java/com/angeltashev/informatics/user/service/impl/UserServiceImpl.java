package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.UserHomeViewModel;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserVisitViewModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
        this.userRepository.saveAndFlush(user);
        return true;
    }

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
        if (userEntity == null) throw new PageNotFoundException("Username is not found");
        UserVisitViewModel userVisitViewModel = this.modelMapper.map(
                userEntity,
                UserVisitViewModel.class
        );
        userVisitViewModel.setAuthority(getUserAuthority(userEntity));
        byte[] profilePicture = userEntity.getProfilePicture();
        String profilePictureString = "";
        if (profilePicture != null) {
            profilePictureString = Base64.getEncoder().encodeToString(userEntity.getProfilePicture());
        }
        userVisitViewModel.setProfilePictureString(profilePictureString);
        return userVisitViewModel;
    }

    @Override
    public UserHomeViewModel getUserHomeDetails(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(new UserEntity());
        UserHomeViewModel userHomeViewModel = this.modelMapper.map(
                userEntity,
                UserHomeViewModel.class
        );
        System.out.println();
        return userHomeViewModel;
    }

    @Override
    public UserDTO findByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElse(null);
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElse(null);
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public void uploadPicture(String username, MultipartFile file) throws FileStorageException {
        // TODO Remove username param; Fix file storage exception
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
}
