package com.adep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.ReturnEntity;

@Repository
public interface ReturnRepository extends CrudRepository<ReturnEntity, Long> {

}
