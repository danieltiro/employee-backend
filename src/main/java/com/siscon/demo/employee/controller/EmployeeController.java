package com.siscon.demo.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siscon.demo.employee.dto.EmployeeCreateDTO;
import com.siscon.demo.employee.dto.EmployeeResultDTO;
import com.siscon.demo.employee.dto.EmployeeUpdateDTO;
import com.siscon.demo.employee.entity.Employee;
import com.siscon.demo.employee.exception.DuplicateResourceException;
import com.siscon.demo.employee.exception.GlobalExceptionHandler.ErrorResponse;
import com.siscon.demo.employee.exception.ServiceException;
import com.siscon.demo.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	@Operation(summary = "Creates an employee", description = "Returns a created employee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
			@ApiResponse(responseCode = "409", description = "Conflict"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@PostMapping
	public ResponseEntity<Employee> create(@Valid @RequestBody EmployeeCreateDTO entityDTO) {
		Employee employee;
		try {
			employee = employeeService.save(entityDTO.toEmployee());
		} catch(DataIntegrityViolationException e) {
			throw new DuplicateResourceException("Employee already exists: " + entityDTO.getDni());
		} catch(Exception e) {
			throw new ServiceException(ExceptionUtils.getRootCauseMessage(e));
		}						
		return ResponseEntity.status(HttpStatus.CREATED).body(employee);
	}
	
	@Operation(summary = "Returns all employees with pagination")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Page of employees",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = Pageable.class)
	                )),
	    @ApiResponse(responseCode = "400", description = "Bad Request")
	})
	@GetMapping("/all")
	public ResponseEntity<Page<Employee>> findAll(
			@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
					@SortDefault(sort = "name", direction = Direction.ASC) }) Pageable pageable) {
		Page<Employee> students = employeeService.findAll(pageable);
		return ResponseEntity.ok(students);
	}
	
	@Operation(summary = "Deletes an employee", description = "Change active/inactive an employee (Soft delete)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Employee was delete"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(name = "id", required = true) String id) {
		employeeService.delete(java.util.UUID.fromString(id));
	}
	
	@Operation(summary = "Update an employee", description = "Returns an employee updated")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
	@PutMapping
	public ResponseEntity<Employee> update(@Valid @RequestBody EmployeeUpdateDTO entityDTO) {
		Employee employee;
		try {
			employee = employeeService.update(entityDTO.toEmployee());
		} catch(Exception e) {
			throw new ServiceException(ExceptionUtils.getRootCauseMessage(e));
		}						
		return ResponseEntity.ok(employee);
	}
	
	@Operation(summary = "Creates one or more employee(s)", description = "Returns one o more created employee(s)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Created employee(s)", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmployeeResultDTO.class)))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping("/save-all")
	public ResponseEntity<List<EmployeeResultDTO>> save(@Valid @RequestBody List<EmployeeCreateDTO> entitiesDTO) {
		List<Employee> employees = new ArrayList<>();
		entitiesDTO.stream().forEach(entityDTO -> employees.add(entityDTO.toEmployee()));
		return ResponseEntity.ok(employeeService.save(employees));
	}

}
