package com.adep.service;

import com.adep.dto.UserDto;

public interface UserService {

	UserDto saveUser(UserDto userDto);

	UserDto getUser(String userName);

	UserDto getUserByUserId(String userId);
	
}
