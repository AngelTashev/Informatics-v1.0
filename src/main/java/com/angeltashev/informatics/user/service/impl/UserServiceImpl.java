package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
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
import java.util.Optional;
import java.util.Set;

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
        byte[] profilePicture = userEntity.getProfilePicture();
        String profilePictureString = "";
        if (profilePicture != null) {
            profilePictureString = Base64.getEncoder().encodeToString(userEntity.getProfilePicture());
        }
        userProfileViewModel.setProfilePictureString(profilePictureString);
        return userProfileViewModel;
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
}
