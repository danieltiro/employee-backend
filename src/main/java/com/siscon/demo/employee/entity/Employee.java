package com.siscon.demo.employee.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.siscon.demo.employee.utility.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Table(name = Constants.DB_PREFIX + "_employee", uniqueConstraints = {
		@UniqueConstraint(name = "idxEmployee_Unique", columnNames = { Constants.DB_PREFIX + "_dni"})
})
@Entity
public class Employee implements Serializable {

	
	private static final long serialVersionUID = -2868932650130787786L;

	@Id
	@Column(name = Constants.DB_PREFIX + "_uuid", updatable = false)
	@UuidGenerator
	private UUID id;
	
	@NotBlank
	@Size(max = 255)
	@Column(name = Constants.DB_PREFIX + "_firstname", nullable = false, length = 255)
	private String firstname;
	
	@Size(max = 255)
	@Column(name = Constants.DB_PREFIX + "_lastname", length = 255)
	private String lastname;
	
	@Size(max = 255)
	@Column(name = Constants.DB_PREFIX + "_middlename", length = 255)
	private String middlename;
	
	@NotBlank
	@Size(max = 255)
	@Column(name = Constants.DB_PREFIX + "_name", nullable = false, length = 255)
	private String name;
	
	@NotBlank
	@Size(max = 1)
	@Column(name = Constants.DB_PREFIX + "_genre", nullable = false, length = 1)
	private String genre;
	
	@NotNull
	@Past
	@Column(name = Constants.DB_PREFIX + "_birthdate", nullable = false)
	private LocalDate birthdate;
	
	@NotBlank
	@Size(max = 25)
	@Column(name = Constants.DB_PREFIX + "_dni", nullable = false, length = 25)
	private String dni;
	
	@NotBlank
	@Size(max = 255)
	@Column(name = Constants.DB_PREFIX + "_position", nullable = false, length = 255)
	private String position;
	
	@NotNull
	@Column(name = Constants.DB_PREFIX + "_created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = Constants.DB_PREFIX + "_deleted_at")
	private LocalDateTime deletedAt;
	
	@Column(name = Constants.DB_PREFIX + "_modified_at")
	private LocalDateTime updatedAt;
	
	@NotNull
	@Column(name = Constants.DB_PREFIX + "_active", nullable = false)
	private boolean active = true;
	
	public Employee() {
		super();
	}
	
	public Employee (String name, String middlename, String firstname, String lastname, String genre, LocalDate birthdate, String dni, String position, boolean active) {
		this.name = name;
		this.middlename = middlename;
		this.firstname = firstname;
		this.lastname = lastname;
		this.genre = genre;
		this.birthdate = birthdate;
		this.dni = dni;
		this.position = position;
		this.active = active;	
	}
	
	public Employee (UUID id, String name, String middlename, String firstname, String lastname, String genre, LocalDate birthdate, String dni, String position, boolean active) {
		this.id = id;
		this.name = name;
		this.middlename = middlename;
		this.firstname = firstname;
		this.lastname = lastname;
		this.genre = genre;
		this.birthdate = birthdate;
		this.dni = dni;
		this.position = position;
		this.active = active;
	}
	
	public int getAge(){
		Period period = Period.between(this.birthdate, LocalDate.now());
		return period.getYears();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(dni, other.dni);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dni);
		return result;
	}
	
	@Override
    public String toString() {
        return String.format("%s %s %s %s %s %i", this.name, this.lastname, this.dni, this.position, this.getAge());
    }
	
}
