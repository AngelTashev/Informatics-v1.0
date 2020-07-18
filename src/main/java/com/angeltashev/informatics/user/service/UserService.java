package com.angeltashev.informatics.user.service;

import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel);

    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
}
