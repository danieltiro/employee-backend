package com.siscon.demo.employee.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.siscon.demo.employee.entity.Employee;


public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID>, JpaRepository<Employee, UUID>{

	Optional<Employee> findByDniIgnoreCase(String dni);
    Optional<Employee> findByDniIgnoreCaseAndActive(String dni, boolean active);
    Page<Employee> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);
    Page<Employee> findByNameIgnoreCaseAndLastnameIgnoreCaseAndActive(String name, String lastname,
			Boolean active, Pageable pageable);
}