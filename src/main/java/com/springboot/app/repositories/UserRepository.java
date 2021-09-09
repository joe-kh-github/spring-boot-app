package com.springboot.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.app.entities.User;

public interface UserRepository extends CrudRepository<User, String> {

	User findByUsername(String username);
}
