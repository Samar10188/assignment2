package com.adep.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.adep.dto.UserDto;

public interface UserService  extends UserDetailsService{

	UserDto saveUser(UserDto userDto);

	UserDto getUser(String userName);

	UserDto getUserByUserId(String userId);
	
}
