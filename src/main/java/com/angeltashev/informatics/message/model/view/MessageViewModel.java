package com.angeltashev.informatics.message.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageViewModel {

    private String id;

    private String fullName;

    private String email;

    private String message;
}

