package com.springboot.app.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.app.common.Utils;
import com.springboot.app.converters.UserMapper;
import com.springboot.app.entities.User;
import com.springboot.app.models.CountryVO;
import com.springboot.app.models.UserVO;
import com.springboot.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUsername(String username) throws Exception {
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new Exception(Utils.getMessageForLocale("user.user_does_not_exist"));

		return user;
	}

	public void save(UserVO userVO) throws Exception {

		LocalDate birthDate = userVO.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateNow = LocalDate.now();
		int age = Period.between(birthDate, dateNow).getYears();
		// if is not adult and is not French then throw an exception
		if (age < 18 || !userVO.getCountryOfResidence().toLowerCase().trim()
				.equals(CountryVO.COUNTRIES.FRANCE.toString().toLowerCase().trim()))
			throw new Exception(Utils.getMessageForLocale("user.you_are_not_allowed_to_create_an_account"));

		// check if user exists
		User user = userRepository.findByUsername(userVO.getUsername());
		if (user != null)
			throw new Exception(Utils.getMessageForLocale("user.user_is_already_created"));

		user = UserMapper.toEntity(userVO);

		userRepository.save(user);
	}
}
