package com.example.lab7.controller.service;

import com.example.lab7.dto.UserDto;
import com.example.lab7.entity.User;
import com.example.lab7.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserServiceTestGetByUsername {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUsersByUsername_HappyPath() {
        String username = "testUser";
        List<User> userList = Arrays.asList(
                new User(1L, username, "John Doe", "Admin"),
                new User(2L, username, "Jane Doe", "User")
        );

        when(userRepository.findUserByUsername(anyString())).thenReturn(userList);

        List<UserDto> resultDtoList = userService.getUsersByUsername(username);

        verify(userRepository).findUserByUsername(username);
        assertThat(resultDtoList).hasSize(2);
        assertThat(resultDtoList.get(0).getUsername()).isEqualTo(username);
        assertThat(resultDtoList.get(1).getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetUsersByUsername_UnhappyPath_RepositoryFailure() {
        String username = "testUser";

        when(userRepository.findUserByUsername(anyString())).thenThrow(new RuntimeException("Query failed"));

        assertThatThrownBy(() -> userService.getUsersByUsername(username))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Query failed");

        verify(userRepository).findUserByUsername(username);
    }
}
