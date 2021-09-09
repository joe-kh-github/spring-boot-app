package com.springboot.app;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;
import com.springboot.app.services.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest

public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void whenGetNonUser_thenThrowExcpetion() throws Exception {

		System.out.println("whenGetNonUser_thenThrowExcpetion");
		String name = "john";
		User userVO = new User();
		userVO.setUsername(name);
		Mockito.when(userService.findByUsername(name)).thenReturn(userVO);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/user/" + name)
				.contentType(MediaType.APPLICATION_JSON);

		Assertions.assertThatThrownBy(() -> mockMvc.perform(mockRequest)).hasCauseInstanceOf(NullPointerException.class)
				.hasMessageContaining("Request processing failed");
	}

	@Test
	public void whenPostUser_thenReturnResponse() throws Exception {

		System.out.println("whenPostUser_thenReturnResponse");

		UserVO userVO = new UserVO();
		String dateStr = "2000-12-12";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		userVO.setUsername("john");
		userVO.setBirthDate(date);
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+122342342343");
		userVO.setGender("male");

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userVO));

		mockMvc.perform(mockRequest).andExpect(status().isCreated());
	}

	@Test
	public void whenPostUserWithoutUsername_thenReturnInvalidRequest() throws Exception {

		System.out.println("whenPostUserWithoutUsername_thenReturnInvalidRequest");

		UserVO userVO = new UserVO();
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+122342342343");
		userVO.setGender("male");

		String json = new ObjectMapper().writeValueAsString(userVO);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
	}

	@Test
	public void whenPostUserWithInvalidPhoneNb_thenReturnInvalidRequest() throws Exception {

		System.out.println("whenPostUserWithInvalidPhoneNb_thenReturnInvalidRequest");

		UserVO userVO = new UserVO();
		userVO.setUsername("john");
		userVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-12"));
		userVO.setCountryOfResidence("France");
		userVO.setPhoneNumber("+123");
		userVO.setGender("male");

		String json = new ObjectMapper().writeValueAsString(userVO);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
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

		Mockito.when(userService.findByUsername(name)).thenReturn(user);

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
		System.out.println("whenEndpointDoesNotExist_thenReturnPageNotFound");
		mockMvc.perform(MockMvcRequestBuilders.get("/test").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
	}
}
