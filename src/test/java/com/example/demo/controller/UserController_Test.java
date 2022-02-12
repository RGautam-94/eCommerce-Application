package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserController_Test {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void SetUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }


    @Test
    public void create_user_happy_path() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void findByUserName_Test() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test-password");
        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", response.getBody().getUsername());

    }

    @Test
    public void findById_Test() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test-password");
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0L, response.getBody().getId());
    }

    @Test
    public void createUser_shortPassword_Test() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test"); // The password is less than 7
        r.setConfirmPassword("test");
        when(encoder.encode("test")).thenReturn("thisIsHashed");
        final ResponseEntity<User> response = userController.createUser(r);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByUserName_Error_Test() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test-password");
        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("bad_username");// Different username
        assertEquals(404, response.getStatusCodeValue());

    }


}
