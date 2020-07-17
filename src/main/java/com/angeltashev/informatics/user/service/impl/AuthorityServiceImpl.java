package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.service.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public void seedAuthorities() {
        this.authorityRepository.save(new AuthorityEntity("ROLE_ROOT_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_STUDENT"));
    }
}
