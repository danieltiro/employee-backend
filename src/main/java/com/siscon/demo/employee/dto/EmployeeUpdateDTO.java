package com.siscon.demo.employee.dto;

import java.util.Objects;
import java.util.UUID;

import com.siscon.demo.employee.entity.Employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para actualizar un empleado")
public class EmployeeUpdateDTO extends EmployeeCreateDTO {
	
	public EmployeeUpdateDTO() {
		super();
	}

    @Schema(
        description = "id", 
        example = "457d3f65-f366-4363-8dbd-23dae4dc7c60", 
        required = true
    )
    @NotNull(message = "El id es obligatorio")
    protected UUID id;
    
    public Employee toEmployee() {
    	return new Employee(this.id, this.name, this.middlename, this.firstname, this.lastname, this.genre, this.birthdate, this.dni, this.position, this.active);
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