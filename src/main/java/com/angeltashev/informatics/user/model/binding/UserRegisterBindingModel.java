package com.angeltashev.informatics.user.model.binding;

import com.angeltashev.informatics.user.validation.annotation.FullName;
import com.angeltashev.informatics.user.validation.annotation.Password;
import com.angeltashev.informatics.user.validation.annotation.Username;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserRegisterBindingModel {

    @NotNull
    @FullName(message = "Full name must start with an uppercase character")
    @Length(min = 4, max = 30, message = "Full name must be between 4 and 30 characters")
    private String fullName;

    @NotNull
    @Username(message = "Username must contain only lowercase characters, numbers and . or _")
    @Length(min = 6, max = 20, message = "Username length must be between 6 and 20 characters")
    private String username;

    @NotNull(message = "Email field cannot be empty")
    @Email
    private String email;

    @NotNull
    @Password(message = "Password must only contain letters and special characters ($, *, .)")
    @Length(min = 8, max = 30, message = "Password length must be between 8 and 30 characters")
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull(message = "Please select your grade")
    private Integer grade;

    @NotNull(message = "Please select your class")
    @Length(min = 1, message = "Please select your class")
    private String gradeClass;

    @Length(max = 30, message = "Phrase length cannot be more than 30 characters")
    private String phrase;

}
