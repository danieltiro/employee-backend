package com.siscon.demo.employee.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siscon.demo.employee.entity.Employee;
import com.siscon.demo.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	@Operation(summary = "Find active employee by DNI", description = "Returns one active employee based on their DNI")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/by-dni")
	public ResponseEntity<Employee> findByDni(
			@Parameter(description = "Employee dni to search for", required = true) @RequestParam String dni) {

		return employeeService.findByDniIgnoreCaseAndActive(dni, true).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "returns an active employees paged")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Page of active employees found",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = Pageable.class)
	                )),
	    @ApiResponse(responseCode = "400", description = "Bad Request"),
	    @ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@GetMapping("/name")
	public ResponseEntity<Page<Employee>> findByName(
			@Parameter(description = "Employee name to search for", required = true) @RequestParam String name,
			@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
					@SortDefault(sort = "name", direction = Direction.ASC) }) Pageable pageable) {

		Page<Employee> students = employeeService.findByNameContainingIgnoreCaseAndActive(name, true, pageable);
		return ResponseEntity.ok(students);
	}

	@Operation(summary = "Create an employee", description = "Returns a created employee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@PostMapping
	public ResponseEntity<Employee> create(@Valid @RequestBody Employee entity) {

		Employee employee = employeeService.save(entity);
		return ResponseEntity.ok(employee);
	}

}
