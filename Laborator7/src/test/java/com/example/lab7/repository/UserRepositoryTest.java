package com.example.lab7.repository;

import com.example.lab7.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    User user;

    @BeforeEach
    void setUp() {
        user = new User(1L,"Chris", "Cristian", "Basic_Admin");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        userRepository.deleteAll();
    }

    // Happy use case
    @Test
    public void testFindByUsername_Found() {
        List<User> userList = userRepository.findUserByUsername("Chris");
        assertThat(userList.get(0).getId()).isEqualTo(user.getId());
        assertThat(userList.get(0).getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testAddUser_Success() {
        user = new User(2L,"Mike", "Mikee", "Basic_Admin");
        userRepository.save(user);
        List<User> userList = userRepository.findUserByUsername("Mike");
        assertThat(!userList.isEmpty()).isTrue();
    }

    @Test
    public void testGetByType_Success(String username, String type) {
        user = new User(3L,"Mike", "Mikee", "Admin");
        userRepository.save(user);
        List<User> userList = userRepository.findUserByCustomQuery(username, type)
                    .stream()
                    .collect(Collectors.toList());
        assertThat(userList.get(0).getUserType()).isEqualTo(type);
        assertThat(userList.get(0).getUsername()).isEqualTo(username);
        }



    // Failure use case
    @Test
    public void testFindByUsername_NotFound() {
        List<User> userList = userRepository.findUserByUsername("Mike");
        assertThat(userList.isEmpty()).isTrue();
    }

    @Test
    public void testAddUser_Failure() {
        user = new User(1L,"Mike", "Mikee", "Basic_Admin");
        userRepository.save(user);
        List<User> userList = userRepository.findUserByUsername("Mike");
        assertThat(userList.isEmpty()).isTrue();
    }

    @Test
    public void testGetByType_InvalidType() {
        String username = "testUsername";
        String invalidType = null; // Invalid type parameter

        user = new User(3L,"Mike", "Mikee", "Admin");
        userRepository.save(user);
        List<User> userList = userRepository.findUserByCustomQuery(username, (String) invalidType)
                .stream()
                .collect(Collectors.toList());
        assertThat(userList.get(0).getUserType()).isEqualTo(invalidType);
        assertThat(userList.get(0).getUsername()).isEqualTo(username);
    }


}
