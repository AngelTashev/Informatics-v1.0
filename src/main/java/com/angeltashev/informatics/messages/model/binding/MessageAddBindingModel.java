package com.angeltashev.informatics.messages.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class MessageAddBindingModel {

    @NotNull
    @Length(min = 3, max = 30, message = "Full name must be between 3 and 30 characters long")
    private String fullName;

    @NotNull
    @Email(message = "Email is not valid")
    private String email;

    @NotNull
    @Length(min = 12, max = 400, message = "Message must be between 12 and 400 characters long")
    private String message;
}
