package com.siscon.demo.employee.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.siscon.demo.employee.dto.EmployeeResultDTO;
import com.siscon.demo.employee.entity.Employee;

public interface EmployeeService {
    Employee save(Employee entity);
    
    Page<Employee> findAll(Pageable pageable);
    void delete(UUID id);
    Employee update(Employee entity);
    List<EmployeeResultDTO> save(List<Employee> employees);
}
