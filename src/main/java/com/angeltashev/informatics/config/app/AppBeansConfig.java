package com.angeltashev.informatics.config.app;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Assignment Home View Mapping
        TypeMap<AssignmentEntity, AssignmentHomeViewModel> assignmentHomeViewMap = modelMapper
                .createTypeMap(AssignmentEntity.class, AssignmentHomeViewModel.class)
                .addMapping(AssignmentEntity::getId,AssignmentHomeViewModel::setId)
                .addMapping(AssignmentEntity::getName, AssignmentHomeViewModel::setName)
                .addMapping(AssignmentEntity::getDescription, AssignmentHomeViewModel::setDescription);

        assignmentHomeViewMap.includeBase(AssignmentEntity.class, AssignmentHomeViewModel.class);
        return modelMapper;
    }
}
