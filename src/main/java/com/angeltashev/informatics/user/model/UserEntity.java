package com.angeltashev.informatics.user.model;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.file.model.DBFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(name = "uuid-string", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_date", nullable = false )
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm")
    private LocalDateTime registrationDate;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    private Set<AuthorityEntity> authorities = new HashSet<>();

    @Column(name="grade", nullable = false)
    private Integer grade;

    @Column(name = "class", nullable = false)
    private String gradeClass;

    @Column(name = "phrase")
    private String phrase;

    @Column(name = "points", nullable = false)
    private Integer points;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "user",
            targetEntity = AssignmentEntity.class
    )
    private Set<AssignmentEntity> assignments;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    private DBFile profilePicture;


    // Security
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
