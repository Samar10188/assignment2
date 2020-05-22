package com.adep.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adep.dto.DemoDto;
import com.adep.entity.DemoEntity;
import com.adep.repository.DemoRepository;
import com.adep.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoRepository demoRepository;

	@Override
	public DemoDto save(DemoDto demoDto) {

		DemoEntity demoEntity = new DemoEntity();

		BeanUtils.copyProperties(demoDto, demoEntity);

		DemoEntity storedDemoEntity = demoRepository.save(demoEntity);

		DemoDto storedDemoDto = new DemoDto();

		BeanUtils.copyProperties(storedDemoEntity, storedDemoDto);

		return storedDemoDto;
	}

}
