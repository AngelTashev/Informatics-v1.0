package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public UserDTO findByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElse(null);
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElse(null);
        return user != null ? this.modelMapper.map(user, UserDTO.class) : null;
    }
}
