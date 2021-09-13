package com.springboot.app;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;

import org.bson.types.ObjectId;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.app.common.Utils;
import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;
import com.springboot.app.repositories.UserRepository;
import com.springboot.app.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	@BeforeEach
	public void init() {
	}

	@Test
	void whenUserNull_thenThrowBadRequestException() throws Exception {

		when(userRepository.findByUsername(anyString())).thenReturn(null);

		Exception exception = null;
		try {
			userServiceImpl.findByUsername("john");
		} catch (Exception ex) {
			exception = ex;
		}
		assertTrue(exception.getMessage().contains("400"));
		assertTrue(exception.getMessage().contains(Utils.getMessageForLocale("user.user_does_not_exist")));
	}

	@Test
	void whenUserExist_thenReturnUser() throws Exception {

		User user = new User();
		user.setID(new ObjectId("6139e4fd624e636dce582a51"));
		user.setUsername("john");
		user.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		user.setCountryOfResidence("France");
		user.setPhoneNumber("+122342342343");
		user.setGender(User.GENDER.MALE);

		when(userRepository.findByUsername("john")).thenReturn(user);

		User returnedUser = userServiceImpl.findByUsername("john");
		
		assertNotNull(returnedUser);
		assertEquals("john", returnedUser.getUsername());
		assertEquals("male", returnedUser.getGender().toString().toLowerCase());
	}

	@Test
	void whenUserIsNotAdult_thenThrowBadRequestException() throws Exception {

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+12345678910");
		userVO.setGender("male");

		Exception exception = null;
		try {
			userServiceImpl.save(userVO);
		} catch (Exception ex) {
			exception = ex;
		}
		assertTrue(exception.getMessage().contains("400"));
		assertTrue(exception.getMessage()
				.contains(Utils.getMessageForLocale("user.you_are_not_allowed_to_create_an_account")));
	}

	@Test
	void whenUserIsAdultAndNotFrench_thenThrowBadRequestException() throws Exception {

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		userVO.setCountryOfResidence("Germany");
		userVO.setPhoneNumber("+12345678910");
		userVO.setGender("male");

		Exception exception = null;
		try {
			userServiceImpl.save(userVO);
		} catch (Exception ex) {
			exception = ex;
		}
		assertTrue(exception.getMessage().contains("400"));
		assertTrue(exception.getMessage()
				.contains(Utils.getMessageForLocale("user.you_are_not_allowed_to_create_an_account")));
	}

	@Test
	void whenUserIsAdultAndIsFrenchButExists_thenThrowBadRequestException() throws Exception {

		User user = new User();
		user.setID(new ObjectId("6139e4fd624e636dce582a51"));
		user.setUsername("john");
		user.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		user.setCountryOfResidence("France");
		user.setPhoneNumber("+122342342343");
		user.setGender(User.GENDER.MALE);

		when(userRepository.findByUsername("john")).thenReturn(user);

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+12345678910");
		userVO.setGender("male");

		Exception exception = null;
		try {
			userServiceImpl.save(userVO);
		} catch (Exception ex) {
			exception = ex;
		}
		assertTrue(exception.getMessage().contains("400"));
		assertTrue(exception.getMessage().contains(Utils.getMessageForLocale("user.user_is_already_created")));
	}

	@Test
	void whenUserIsAdultAndIsFrenchButNotExists_thenThrowBadRequestException() throws Exception {

		when(userRepository.findByUsername("john")).thenReturn(null);

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+12345678910");
		userVO.setGender("male");

		userServiceImpl.save(userVO);

	}
}
