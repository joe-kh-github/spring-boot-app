package com.springboot.app.services;

import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;

public interface UserService {

	User findByUsername(String username) throws Exception;

	void save(UserVO userVO) throws Exception;
}