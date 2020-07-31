package com.angeltashev.informatics.user.service;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.UserHomeViewModel;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserRoleViewModel;
import com.angeltashev.informatics.user.model.view.UserVisitViewModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel);

    UserProfileViewModel getUserProfile(String username);
    UserVisitViewModel getUserVisitProfile(String username) throws UsernameNotFoundException;
    UserHomeViewModel getUserHomeDetails(String username);

    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);

    List<UserAssignmentAddBindingModel> getUserAssignmentAddModels();

    boolean uploadProfilePicture(String username, MultipartFile file) throws FileStorageException, IOException;

    UserDTO getUserDTO(String username);

    boolean addPointsToUser(String username, Integer points);

    List<UserEntity> updateAllStudents();

    List<UserRoleViewModel> getAllAdmins();

    List<UserRoleViewModel> getAllStudents();

    boolean demoteAdminById(String adminId);

    boolean promoteStudentById(String studentId);

    boolean changePhrase(String username, String phrase);
}
