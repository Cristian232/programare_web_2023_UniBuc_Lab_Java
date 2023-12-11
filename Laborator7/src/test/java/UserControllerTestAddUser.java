import com.example.lab7.controller.UserController;
import com.example.lab7.dto.UserDto;
import com.example.lab7.dto.UserDtoTestProxy;
import com.example.lab7.entity.User;
import com.example.lab7.controller.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTestAddUser {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDtoTestProxy userDtoTestProxy;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetUser_HappyPath() {
        // Arrange
        String username = "testUsername";
        List<UserDto> userDtos = Arrays.asList(
                new UserDto("user1", "John Doe", "Admin"),
                new UserDto("user2", "Jane Doe", "User")
        );

        // Mock the behavior of userService.getUsersByUsername
        when(userService.getUsersByUsername(username)).thenReturn(userDtos);

        // Act
        ResponseEntity<List<UserDto>> responseEntity = userController.getUser(username);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDtos, responseEntity.getBody());

        // Verify that userService.getUsersByUsername was called with the correct parameter
        verify(userService, times(1)).getUsersByUsername(username);
    }

    @Test
    public void testGetUser_UnhappyPath_UserNotFound() {
        // Arrange
        String username = "nonexistentUser";

        // Mock the behavior of userService.getUsersByUsername
        when(userService.getUsersByUsername(username)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<UserDto>> responseEntity = userController.getUser(username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());

        // Verify that userService.getUsersByUsername was called with the correct parameter
        verify(userService, times(1)).getUsersByUsername(username);
    }

    @Setter
    @Getter
    class UserDto {
        private String username;
        private String fullName;
        private String userType;

        public UserDto(String user1, String johnDoe, String admin) {
        }
    }

}
