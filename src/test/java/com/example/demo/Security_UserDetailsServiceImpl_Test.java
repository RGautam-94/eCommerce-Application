package com.example.demo;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Security_UserDetailsServiceImpl_Test {
    private UserDetailsServiceImpl userDetailsService;
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        userDetailsService = new UserDetailsServiceImpl();
        TestUtils.injectObjects(userDetailsService, "userRepository", userRepository);
    }
    @Test
    public void loadUserByUsername_Test(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("test-password");
        when(userRepository.findByUsername("test")).thenReturn(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername("test");
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(userDetails);
        assertNotNull(authorities);
        assertEquals("test", userDetails.getUsername());
        assertEquals("test-password", userDetails.getPassword());
    }

}
