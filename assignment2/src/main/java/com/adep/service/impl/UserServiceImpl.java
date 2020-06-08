package com.adep.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adep.dto.UserDto;
import com.adep.entity.UserEntity;
import com.adep.exception.UserServiceException;
import com.adep.repository.UserRepository;
import com.adep.service.UserService;
import com.adep.shared.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Utils utils;

	@Override
	public UserDto saveUser(UserDto userDto) {

		UserEntity storedUserDetails = userRepository.findByUsername(userDto.getUsername());
		if (storedUserDetails != null)
			throw new UserServiceException("Username already exists, please try with different username!");

		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userDto, user);

		user.setUserId(utils.generateUserId(20));
//		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		UserEntity storedUser = userRepository.save(user);

		UserDto storedUserDto = new UserDto();
		BeanUtils.copyProperties(storedUser, storedUserDto);

		return storedUserDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity storedUserDetails = userRepository.findByUsername(username);

		if (storedUserDetails == null)
			throw new UsernameNotFoundException(username);

		return new User(storedUserDetails.getUsername(), storedUserDetails.getPassword(), new ArrayList<>());

	}

	@Override
	public UserDto getUser(String username) {

		UserEntity storedUserDetails = userRepository.findByUsername(username);

		/*
		 * if (storedUserDetails == null) throw new UsernameNotFoundException(username);
		 */

		UserDto storedUserDto = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, storedUserDto);

		return storedUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity storedUserDetails = userRepository.findByUserId(userId);

		/*
		 * if (storedUserDetails == null) throw new
		 * UsernameNotFoundException("User with userId: " + userId + " not found");
		 */
		UserDto storedUserDto = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, storedUserDto);
		return storedUserDto;
	}

}
