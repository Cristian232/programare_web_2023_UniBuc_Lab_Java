package com.example.lab7;
import com.example.lab7.controller.UserController;
import com.example.lab7.dto.UserDto;
import com.example.lab7.controller.service.UserService;
import com.example.lab7.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


//Happy Path: The test cases simulate a scenario where everything is working as expected.
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	@Before
	public void setUp() {
        // No setup here :)
	}

	@Test
	public void testGetUser_HappyPath() {
		String username = "testUsername";
		List<UserDto> userDtos = Arrays.asList(
				new User(1L, "user1", "John Doe", "Admin"),
				new UserDto(2L, "user2", "Jane Doe", "User")
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

	@Test
	public void testAddUser_HappyPath() {
		// Arrange
		UserDto userDto = new UserDto(1L, "user1", "John Doe", "Admin");

		// Mock the behavior of userService.addUser
		when(userService.addUser(userDto)).thenReturn(userDto);

		// Act
		ResponseEntity<UserDto> responseEntity = userController.addUser(userDto);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userDto, responseEntity.getBody());

		// Verify that userService.addUser was called with the correct parameter
		verify(userService, times(1)).addUser(userDto);
	}

	@Test
	public void testAddUser_UnhappyPath_InvalidUser() {
		// Arrange
		UserDto invalidUserDto = new UserDto(); // Invalid user with missing required fields

		// Act
		ResponseEntity<UserDto> responseEntity = userController.addUser(invalidUserDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());

		// Verify that userService.addUser was not called
		verify(userService, never()).addUser(invalidUserDto);
	}

	@Test
	public void testGetByType_HappyPath() {
		// Arrange
		String username = "testUsername";
		String type = "testType";
		List<UserDto> userDtos = Arrays.asList(
				new UserDto(1L, "user1", "John Doe", "Admin"),
				new UserDto(2L, "user2", "Jane Doe", "User")
		);

		// Mock the behavior of userService.getUserByUsernameAndType
		when(userService.getUserByUsernameAndType(username, type)).thenReturn(userDtos);

		// Act
		ResponseEntity<List<UserDto>> responseEntity = userController.getByType(username, type);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userDtos, responseEntity.getBody());

		// Verify that userService.getUserByUsernameAndType was called with the correct parameters
		verify(userService, times(1)).getUserByUsernameAndType(username, type);
	}

	@Test
	public void testGetByType_UnhappyPath_InvalidType() {
		// Arrange
		String username = "testUsername";
		String invalidType = null; // Invalid type parameter

		// Act
		ResponseEntity<List<UserDto>> responseEntity = userController.getByType(username, invalidType);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(Collections.emptyList(), responseEntity.getBody());

		// Verify that userService.getUserByUsernameAndType was not called
		verify(userService, never()).getUserByUsernameAndType(username, invalidType);
	}
}
