package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuthorityProcessingServiceImpl implements AuthorityProcessingService {

    private final AuthorityRepository authorityRepository;

    @Override
    public void seedAuthorities() {
        this.authorityRepository.save(new AuthorityEntity("ROLE_ROOT_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_ADMIN"));
        this.authorityRepository.save(new AuthorityEntity("ROLE_STUDENT"));
        log.info("Seed authorities: Seeded all authorities");
    }

    @Override
    public AuthorityEntity getStudentAuthority() {
        log.info("Get student authority: Retrieving student authority");
        return this.authorityRepository.findByAuthority("ROLE_STUDENT");
    }

    @Override
    public AuthorityEntity getRootAdminAuthority() {
        log.info("Get root admin authority: Retrieving root admin authority");
        return this.authorityRepository.findByAuthority("ROLE_ROOT_ADMIN");
    }
}
