package com.siscon.demo.employee.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un nuevo empleado")
public class EmployeeResultDTO {
    @Schema(
        description = "Documento Nacional de Identidad (DNI/RFC)", 
        example = "TIBD841213Q50"
    )
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    
    @Schema(
            description = "ID del nuevo registro", 
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
    private UUID id;
    
    @Schema(
            description = "Mensaje", 
            example = "Registro creado"
        )
    private String message;
    
    @Schema(
            description = "Código de mensaje", 
            example = "200 OK, 500 Error",
            allowableValues = {"200","500"}
        )
    private int code;
    
    public EmployeeResultDTO(String dni, UUID id, int code, String message) {
    	this.dni = dni;
    	this.id = id;
    	this.message = message;
    	this.code = code;    	
    }
}