package com.adep.rest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adep.dto.UserDto;
import com.adep.rest.request.UserModelRequest;
import com.adep.rest.response.UserModelResponse;
import com.adep.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping("/users")
public class UserContoller {

	@Autowired
	private UserService userService;

	@PostMapping
	public UserModelResponse createUser(@RequestBody UserModelRequest userModelRequest) {

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userModelRequest, userDto);
		UserDto storedUserDto = userService.saveUser(userDto);
		UserModelResponse userModelResponse = new UserModelResponse();
		BeanUtils.copyProperties(storedUserDto, userModelResponse);

		return userModelResponse;
	}

	@ApiImplicitParams({ @ApiImplicitParam(name = "authorization", value = "Bearer JWT Token", paramType = "header") })
	@GetMapping("/{userId}")
	public UserModelResponse getUser(@PathVariable("userId") String userid) {

		UserDto storedUserDto = userService.getUserByUserId(userid);
		UserModelResponse userModelResponse = new UserModelResponse();
		BeanUtils.copyProperties(storedUserDto, userModelResponse);

		return userModelResponse;
	}

}
