package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorityProcessingServiceImpl implements AuthorityProcessingService {

    private final AuthorityRepository authorityRepository;

    @Override
    public void seedAuthorities() {
        this.authorityRepository.save(new AuthorityEntity("ROLE_ROOT_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_STUDENT"));
    }

    @Override
    public AuthorityEntity getStudentAuthority() {
        return this.authorityRepository.findByAuthority("ROLE_STUDENT");
    }
}
