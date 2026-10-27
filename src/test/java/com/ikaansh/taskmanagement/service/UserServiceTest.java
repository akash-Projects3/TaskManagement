package com.ikaansh.taskmanagement.service;

import com.ikaansh.taskmanagement.entity.User;
import com.ikaansh.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserShouldNormalizeEmailAndHashPassword() {
        when(userRepository.existsByEmail("akash@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("Password123!"))
                .thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user;
        });

        User result = userService.registerUser(
                " Akash ", " AKASH@EXAMPLE.COM ", "Password123!");

        assertEquals("Akash", result.getName());
        assertEquals("akash@example.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassword());

        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserShouldRejectDuplicateEmail() {
        when(userRepository.existsByEmail("akash@example.com"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(
                        "Akash", "AKASH@example.com", "Password123!"));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void findByEmailShouldReturnUser() {
        User user = new User();
        user.setName("Akash");
        user.setEmail("akash@example.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail("akash@example.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail(" AKASH@EXAMPLE.COM ");

        assertEquals("Akash", result.getName());
        assertEquals("akash@example.com", result.getEmail());
    }

    @Test
    void findByEmailShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());
 
        assertThrows(IllegalArgumentException.class, () ->
                userService.findByEmail("missing@example.com"));
    }
}



