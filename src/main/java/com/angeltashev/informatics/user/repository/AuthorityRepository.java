package com.angeltashev.informatics.user.repository;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String> {

    AuthorityEntity findByAuthority(String authority);
}
