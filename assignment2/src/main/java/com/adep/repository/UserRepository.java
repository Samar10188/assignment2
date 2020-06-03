package com.adep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long>{

	UserEntity findByUsername(String username);
	
	UserEntity findByUserId(String userId);

}
