package com.siscon.demo.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.siscon.demo.employee.entity.Employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Schema(description = "Datos para crear un nuevo empleado")
public class EmployeeCreateDTO {

    @Schema(
        description = "Primer nombre del empleado", 
        example = "Daniel", 
        required = true
    )
    @NotBlank(message = "El nombre es obligatorio")
    protected String name;
    
    @Schema(
            description = "Segundo Nombre del empleado", 
            example = "Joav", 
            required = true
        )
    protected String middlename;

    @Schema(
        description = "Apellido1 del empleado", 
        example = "Tiro", 
        required = true
    )
    @NotBlank(message = "El apellido 1 es obligatorio")
    protected String firstname;
    
    @Schema(
            description = "Apellido 2 del empleado", 
            example = "Bravo"
        )
    protected String lastname;

    @Schema(
        description = "Fecha de nacimiento (formato YYYY-MM-DD)", 
        example = "2020-01-01", 
        type = "string", 
        format = "date"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    protected LocalDate birthdate;

    @Schema(
        description = "Género del empleado (M/F)", 
        example = "M", 
        allowableValues = {"M", "F"}
    )
    @Pattern(regexp = "[MF]", message = "El género debe ser M, F")
    protected String genre;

    @Schema(
        description = "Puesto o cargo del empleado", 
        example = "Java Programmer"
    )
    protected String position;

    @Schema(
        description = "Documento Nacional de Identidad (DNI/RFC)", 
        example = "TIBD841213Q50", 
        required = true
    )
    @NotBlank(message = "El DNI es obligatorio")
    protected String dni;
    
    @Schema(
        description = "Usuario activo / inactivo", 
        example = "true", 
        required = true
    )
    @NotNull(message = "El valor activo es obligatorio")
    protected boolean active;
    
    public Employee toEmployee() {
    	return new Employee(this.name, this.middlename, this.firstname, this.lastname, this.genre, this.birthdate, this.dni, this.position, this.active);
    }
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeUpdateDTO other = (EmployeeUpdateDTO) obj;
		return Objects.equals(dni, other.dni);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dni);
		return result;
	}

}