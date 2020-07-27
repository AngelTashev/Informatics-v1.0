package com.angeltashev.informatics.assignment.model.binding;

import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentAddBindingModel {

    @NotNull
    @Length(min = 8, max = 50, message = "Title length must be between 8 and 50 characters")
    String name;

    @NotNull
    @Length(min = 12, max = 400, message = "Title length must be between 12 and 400 characters")
    String description;

    @NotNull()
    @Size(min = 1, message = "Select at least one student, please :)")
    List<String> users;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "You must specify a due date")
    @FutureOrPresent(message = "Due date must be in the future")
    LocalDateTime dueDate;
}
