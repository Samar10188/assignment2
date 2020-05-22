package com.adep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.DemoEntity;

@Repository
public interface DemoRepository extends CrudRepository<DemoEntity, Long> {

}
