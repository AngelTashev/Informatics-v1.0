package com.angeltashev.informatics.user;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import com.angeltashev.informatics.user.service.impl.AuthorityProcessingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorityProcessingServiceImplTest {

    // Authority
    private String VALID_STUDENT_ID = "studentId";
    private String VALID_STUDENT_AUTHORITY = "ROLE_STUDENT";

    private String VALID_ADMIN_ID = "adminId";
    private String VALID_ADMIN_AUTHORITY = "ROLE_ADMIN";

    private String VALID_ROOT_ADMIN_ID = "rootAdminId";
    private String VALID_ROOT_ADMIN_AUTHORITY = "ROLE_ROOT_ADMIN";

    private AuthorityProcessingService serviceToTest;

    private AuthorityEntity studentAuthority;
    private AuthorityEntity adminAuthority;
    private AuthorityEntity rootAdminAuthority;

    @Mock
    private AuthorityRepository mockAuthorityRepository;

    @BeforeEach
    void setUp() {
        this.serviceToTest = new AuthorityProcessingServiceImpl(mockAuthorityRepository);

        studentAuthority = new AuthorityEntity();
        studentAuthority.setId(VALID_STUDENT_ID);
        studentAuthority.setAuthority(VALID_STUDENT_AUTHORITY);

        adminAuthority = new AuthorityEntity();
        adminAuthority.setId(VALID_ADMIN_ID);
        adminAuthority.setAuthority(VALID_ADMIN_AUTHORITY);

        rootAdminAuthority = new AuthorityEntity();
        rootAdminAuthority.setId(VALID_ROOT_ADMIN_ID);
        rootAdminAuthority.setAuthority(VALID_ROOT_ADMIN_AUTHORITY);
    }

    @Test
    public void seedAllRolesShouldSeedRoles() {
        // Arrange

        // Act
        this.serviceToTest.seedAuthorities();

        // Assert
        verify(this.mockAuthorityRepository, times(3)).save(Mockito.any(AuthorityEntity.class));
    }

    @Test
    public void getStudentAuthorityShouldReturnCorrectAuthority() {
        // Arrange
        when(this.mockAuthorityRepository.findByAuthority(VALID_STUDENT_AUTHORITY))
                .thenReturn(studentAuthority);

        // Act
        AuthorityEntity entity = this.serviceToTest.getStudentAuthority();

        // Assert
        assertEquals(studentAuthority.getId(), entity.getId());
        assertEquals(studentAuthority.getAuthority(), entity.getAuthority());
    }

    @Test
    public void getAdminAuthorityShouldReturnCorrectAuthority() {
        // Arrange
        when(this.mockAuthorityRepository.findByAuthority(VALID_ADMIN_AUTHORITY))
                .thenReturn(adminAuthority);

        // Act
        AuthorityEntity entity = this.serviceToTest.getAdminAuthority();

        // Assert
        assertEquals(adminAuthority.getId(), entity.getId());
        assertEquals(adminAuthority.getAuthority(), entity.getAuthority());
    }

    @Test
    public void getRootAdminAuthorityShouldReturnCorrectAuthority() {
        // Arrange
        when(this.mockAuthorityRepository.findByAuthority(VALID_ROOT_ADMIN_AUTHORITY))
                .thenReturn(rootAdminAuthority);

        // Act
        AuthorityEntity entity = this.serviceToTest.getRootAdminAuthority();

        // Assert
        assertEquals(rootAdminAuthority.getId(), entity.getId());
        assertEquals(rootAdminAuthority.getAuthority(), entity.getAuthority());
    }

}
