package com.angeltashev.informatics.user.service;

import com.angeltashev.informatics.user.model.AuthorityEntity;

public interface AuthorityProcessingService {

    public void seedAuthorities();

    public AuthorityEntity getStudentAuthority();

    public AuthorityEntity getAdminAuthority();

    public AuthorityEntity getRootAdminAuthority();
}
