package com.siscon.demo.employee.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siscon.demo.employee.entity.Employee;
import com.siscon.demo.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	
	@Transactional(readOnly = true)
	@Override
	public Optional<Employee> findByDniIgnoreCase(String dni) {
		Optional<Employee> optEmployee =  employeeRepository.findByDniIgnoreCase(dni);
		return optEmployee;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Employee> findByDniIgnoreCaseAndActive(String dni, boolean active) {
		Optional<Employee> optEmployee =  employeeRepository.findByDniIgnoreCaseAndActive(dni, active);
		return optEmployee;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Employee> findByNameContainingIgnoreCaseAndActive(String name, boolean active, Pageable pageable) {		
		return employeeRepository.findByNameContainingIgnoreCaseAndActive(name, active, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Employee> findByNameIgnoreCaseAndLastnameIgnoreCaseAndActive(String name, String lastname,
			Boolean active, Pageable pageable) {
		return employeeRepository.findByNameIgnoreCaseAndLastnameIgnoreCaseAndActive(name, lastname, active, pageable);
	}
	
	@Transactional
	@Override
	public Employee save(Employee entity) {
		return employeeRepository.save(entity);
	}


}
