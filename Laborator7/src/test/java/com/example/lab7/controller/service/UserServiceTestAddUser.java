package com.example.lab7.controller.service;
import com.example.lab7.dto.UserDto;
import com.example.lab7.entity.User;
import com.example.lab7.mapper.UserMapper;
import com.example.lab7.repository.UserRepository;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTestAddUser {

    @Data
    @Builder
    public class UserDtoTest extends UserDto {
        private String username;
        private String fullName;
        private String userType;
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddUser_HappyPath() {
        UserDtoTest userDtoTest = new UserDtoTest("testUser", "John Doe", "Admin");
        User userToSave = UserMapper.convertToUser(userDtoTest);
        User savedUser = new User(1L, "testUser", "John Doe", "Admin");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto resultDto = userService.addUser(userDtoTest);

        verify(userRepository).save(userToSave);
        assertThat(resultDto).isEqualToComparingFieldByField(userDtoTest);
    }

    @Test
    public void testAddUser_UnhappyPath_SaveFailure() {
        UserDto userDto = new UserDtoTest("testUser", "John Doe", "Admin");
        User userToSave = UserMapper.convertToUser(userDto);

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save failed"));

        assertThatThrownBy(() -> userService.addUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Save failed");

        verify(userRepository).save(userToSave);
    }
}
