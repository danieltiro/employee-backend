package com.siscon.demo.employee.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.siscon.demo.employee.entity.Employee;

public interface EmployeeService {
	Optional<Employee> findByDniIgnoreCase(String dni);
    Optional<Employee> findByDniIgnoreCaseAndActive(String dni, boolean active);
    Page<Employee> findByNameContainingIgnoreCaseAndActive(String name, boolean active, Pageable pageable);
    Page<Employee> findByNameIgnoreCaseAndLastnameIgnoreCaseAndActive(String name, String lastname,
			Boolean active, Pageable pageable);
    Employee save(Employee entity);
}
