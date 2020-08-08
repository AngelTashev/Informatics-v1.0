package com.angeltashev.informatics.user.security;

import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    private String VALID_USERNAME = "validUsername";
    private String VALID_PASSWORD = "validPassword";
    private String VALID_AUTHORITY = "validAuthority";

    @Mock
    private UserRepository userRepository;

    private UserEntity userEntity;
    private AuthorityEntity authorityEntity;

    private UserDetailsServiceImpl serviceToTest;

    @BeforeEach
    void setUp() {
        authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority(VALID_AUTHORITY);

        userEntity = new UserEntity();
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setPassword(VALID_PASSWORD);
        userEntity.setAuthorities(Set.of(authorityEntity));

        this.serviceToTest = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void loadByUsernameShouldLoadCorrectUser() {
        // Arrange
        when(this.userRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = this.serviceToTest.loadUserByUsername(VALID_USERNAME);

        // Assert
        assertEquals(VALID_USERNAME, userDetails.getUsername());
        assertEquals(VALID_PASSWORD, userDetails.getPassword());
    }

    @Test
    public void loadByUsernameShouldThrowUsernameNotFoundExceptionOnInvalidUser() {
        // Arrange
        final String invalidUsername = "invalidUsername";
        when(this.userRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.loadUserByUsername(invalidUsername));
    }
}
