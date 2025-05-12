package com.siscon.demo.employee;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.siscon.demo.employee.entity.Employee;
import com.siscon.demo.employee.repository.EmployeeRepository;
import com.siscon.demo.employee.service.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Test
	void contextLoads() {
	}
	
	@Test
	void testGetAllEmployees() {
		PageRequest pageable = PageRequest.of(0, 10);
		List<Employee> employees = new ArrayList<>();
		Employee employee = new Employee("Daniel", "Tiro", null, null, "M", LocalDate.of(2000, 1, 1), "TIBD841213", "Software Development", true);
		employees.add(employee);
		
		when(employeeRepository.findAll(pageable)).thenReturn(new PageImpl<>(employees, pageable, employees.size()));
		Page<Employee> pageEmployees = employeeService.findAll(pageable);
		assertThat(pageEmployees).isNotNull();
		assertEquals(pageEmployees.getContent().size(), 1);
		assertEquals(pageEmployees.getContent().get(0).getDni(), "TIBD841213");
		assertEquals(pageEmployees.getContent().get(0).getName(), "Daniel");
		verify(employeeRepository).findAll(pageable);
	}

	@Test
	void testSaveEmployee() {
		Employee employee = new Employee("Daniel", "Tiro", null, null, "M", LocalDate.of(2000, 1, 1), "TIBD841213", "Software Development", true);
		when(employeeRepository.save(employee)).thenReturn(employee);
		employeeService.save(employee);
		assertNotNull(employee);
		assertNotNull(employee.getCreatedAt());
		assertNotNull(employee.getName());
		assertNotNull(employee.getDni());
		verify(employeeRepository).save(employee);
	}
	
	@Test
	void testUpdateEmployee() {
		Employee employee = new Employee("Daniel", "Tiro", null, null, "M", LocalDate.of(2000, 1, 1), "TIBD841213", "Software Development", true);
		employee.setId(UUID.randomUUID());
		when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
		when(employeeRepository.save(employee)).thenReturn(employee);
		employeeService.update(employee);
		assertNotNull(employee);
		assertNotNull(employee.getId());
		assertNotNull(employee.getCreatedAt());
		assertNotNull(employee.getName());
		assertNotNull(employee.getDni());
		verify(employeeRepository).save(employee);
	}
}
