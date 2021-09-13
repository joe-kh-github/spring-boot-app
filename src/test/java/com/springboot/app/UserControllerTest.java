package com.springboot.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.controllers.UserController;
import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;
import com.springboot.app.services.UserServiceImpl;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private UserController userController;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@Test
	public void whenGetUser_thenReturnResponse() throws Exception {

		String name = "john";
		User user = new User();
		user.setID(new ObjectId("6139e4fd624e636dce582a51"));
		user.setUsername(name);
		user.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		user.setCountryOfResidence("France");
		user.setPhoneNumber("+122342342343");
		user.setGender(User.GENDER.MALE);

		when(userServiceImpl.findByUsername(name)).thenReturn(user);

		UserVO userVO = userController.fetchByUsername(name);

		assertEquals("john", userVO.getUsername());
		assertEquals("male", userVO.getGender().toString().toLowerCase().trim());
		assertEquals("+122342342343", userVO.getPhoneNumber());
	}

	@Test
	public void whenPostUser_thenReturnResponse() throws Exception {

		UserVO userVO = new UserVO();
		String dateStr = "2000-12-12";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		userVO.setUsername("john");
		userVO.setBirthDate(date);
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+122342342343");
		userVO.setGender("male");

		ResponseEntity<String> resp = userController.save(userVO);

		assertEquals(HttpStatus.CREATED.value(), resp.getStatusCodeValue());
	}

	@Test
	public void whenPostUserWithoutUsername_thenReturnInvalidRequest() throws Exception {

		UserVO userVO = new UserVO();
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+122342342343");
		userVO.setGender("male");

		String json = new ObjectMapper().writeValueAsString(userVO);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		MvcResult mvcResult = mockMvc.perform(mockRequest).andReturn();

		int statusCode = mvcResult.getResponse().getStatus();
		String bodyMessage = mvcResult.getResponse().getContentAsString();

		assertEquals(HttpStatus.BAD_REQUEST.value(), statusCode);
		assertTrue(bodyMessage.contains("user.username.notempty"));
		assertTrue(bodyMessage.contains("user.username.notnull"));
	}

	@Test
	public void whenPostUserWithInvalidPhoneNb_thenReturnInvalidRequest() throws Exception {

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+123");
		userVO.setGender("male");

		String json = new ObjectMapper().writeValueAsString(userVO);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		MvcResult mvcResult = mockMvc.perform(mockRequest).andReturn();

		int statusCode = mvcResult.getResponse().getStatus();
		String bodyMessage = mvcResult.getResponse().getContentAsString();

		assertEquals(HttpStatus.BAD_REQUEST.value(), statusCode);
		assertTrue(bodyMessage.contains("size must be"));
	}

	@Test
	public void whenUserExist_thenReturnResponse() throws Exception {

		System.out.println("whenUserExist_thenReturnResponse");
		String name = "john";
		User user = new User();
		user.setID(new ObjectId("6139e4fd624e636dce582a51"));
		user.setUsername(name);
		user.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-12-12"));
		user.setCountryOfResidence("France");
		user.setPhoneNumber("+122342342343");
		user.setGender(User.GENDER.MALE);

		when(userServiceImpl.findByUsername(name)).thenReturn(user);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/user/" + name).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String resp = mvcResult.getResponse().getContentAsString();
		JSONObject jsonObject = new JSONObject(resp);

		assertEquals("+122342342343", jsonObject.getString("phoneNumber"));
		assertEquals("MALE", jsonObject.getString("gender"));
		assertEquals("6139e4fd624e636dce582a51", jsonObject.getString("id"));
		assertEquals("France", jsonObject.getString("countryOfResidence"));
		assertEquals("2000-12-11T22:00:00.000+00:00", jsonObject.getString("birthDate"));
		assertEquals("john", jsonObject.getString("username"));
	}

	@Test
	public void whenEndpointDoesNotExist_thenReturnPageNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
	}
}
