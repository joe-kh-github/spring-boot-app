package com.springboot.app.converters;

import java.util.Arrays;

import org.apache.commons.lang3.EnumUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.app.common.Utils;
import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;

public class UserMapper {

	public static UserVO toModelVO(User user) {
		UserVO userVO = new UserVO();
		userVO.setId(user.getID().toHexString());
		userVO.setBirthDate(user.getBirthDate());
		userVO.setCountryOfResidence(user.getCountryOfResidence());

		if (!Utils.isEnumEmptyOrNull(user.getGender()))
			userVO.setGender(user.getGender().toString());

		if (!Utils.isEmptyOrNull(user.getPhoneNumber()))
			userVO.setPhoneNumber(user.getPhoneNumber());

		userVO.setUsername(user.getUsername());
		return userVO;
	}

	public static User toEntity(UserVO userVO) throws Exception {
		User user = new User();
		if (!Utils.isEmptyOrNull(userVO.getId()))
			user.setID(new ObjectId(userVO.getId()));

		user.setBirthDate(userVO.getBirthDate());
		user.setCountryOfResidence(userVO.getCountryOfResidence());

		if (!Utils.isEmptyOrNull(userVO.getGender())) {
			if (EnumUtils.isValidEnum(User.GENDER.class, userVO.getGender().toUpperCase().trim()))
				user.setGender(User.GENDER.valueOf(userVO.getGender().toUpperCase()));
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						Utils.getMessageForLocale("enum.invalid") + " " + Arrays.asList(User.GENDER.values()));
		}

		if (!Utils.isEmptyOrNull(userVO.getPhoneNumber()))
			user.setPhoneNumber(userVO.getPhoneNumber());

		user.setUsername(userVO.getUsername());

		return user;
	}
}
