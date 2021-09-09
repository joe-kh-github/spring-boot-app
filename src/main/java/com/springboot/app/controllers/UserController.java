package com.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.converters.UserMapper;
import com.springboot.app.entities.User;
import com.springboot.app.models.UserVO;
import com.springboot.app.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// register a user
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> save(@Valid @RequestBody UserVO userVO) throws Exception {
		userService.save(userVO);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@GetMapping("/{name}")
	public UserVO fetchByUsername(@PathVariable("name") String name) throws Exception {
		User user = userService.findByUsername(name);
		return UserMapper.toModelVO(user);
	}

}