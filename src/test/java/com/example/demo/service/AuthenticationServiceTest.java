package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserServiceImpl;

public class AuthenticationServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addUserSuccess() {
		User user = new User();
		user.setUsername("admin");
		user.setPetname("dog");
		user.setEmail("admin@gmail.com");
		user.setPassword("admin");

		when(userRepository.save(any())).thenReturn(user);

		User savedUser = userService.addUser(user);
		assertEquals(user.getUsername(), savedUser.getUsername());
	}

	@Test
    public void addUserFailure() {
        when(userRepository.save(any())).thenReturn(null);

        User savedUser = userService.addUser(null);
        assertEquals(null, savedUser);
    }

	@Test
	void testLoginUser() {
		String username = "testuser";
		String password = "testpassword";
		String userRole = "user";

		User mockUser = new User();
		when(userRepository.validateUser(username, password, userRole)).thenReturn(mockUser);

		assertTrue(userService.loginUser(username, password, userRole));
	}

	@Test
	void testForgotPassword() {
		String username = "testuser";
		String petname = "fluffy";

		User mockUser = new User();
		mockUser.setId(42); // Set a sample user ID
		when(userRepository.RequestValue(username, petname)).thenReturn(mockUser);

		int userId = userService.forgotPassword(username, petname);
		assertEquals(42, userId);
	}
}
