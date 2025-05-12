package com.siscon.demo.employee.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.siscon.demo.employee.dto.EmployeeResultDTO;
import com.siscon.demo.employee.entity.Employee;
import com.siscon.demo.employee.exception.DuplicateResourceException;
import com.siscon.demo.employee.exception.ResourceNotFoundException;
import com.siscon.demo.employee.exception.ServiceException;
import com.siscon.demo.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	private final PlatformTransactionManager transactionManager;
	
	/**
	 * Crea un nuevo registro de empleado en el sistema.
	 * 
	 * <p>Este método persiste una nueva entidad de empleado en la base de datos, asignándole
	 * un identificador único (UUID) y estableciendo los metadatos de auditoría iniciales como
	 * la fecha de creación y el usuario que lo creó.</p>
	 * 	 * 
	 * <p>Por defecto, todos los empleados nuevos se crean con el estado 'active' en true.
	 * Los campos como 'updatedAt', 'updatedBy', 'deletedAt' y 'deletedBy' se inicializan
	 * como null y se actualizarán posteriormente cuando corresponda.</p>
	 * 
	 * <p><strong>Ejemplo de uso:</strong></p>
	 * <pre>
	 *     Employee newEmployee = new Employee();
	 *     newEmployee.setName("Daniel");
	 *     newEmployee.setLastName("Tiro");
	 *     newEmployee.setDni("12345678A");
	 *     newEmployee.setPosition("Java Developer");
	 *     
	 *     Employee savedEmployee = employeeService.save(newEmployee);
	 * </pre>
	 *
	 * @param entity Objeto Employee con los datos del nuevo empleado.
	 * @return La entidad Employee persistida con su ID asignado y los campos de auditoría establecidos
	 * @throws IllegalArgumentException si la entidad es null o si contiene datos inválidos
	 * @throws DuplicateResourceException si ya existe un empleado con el mismo DNI o correo electrónico
	 * @throws ServiceException si ocurre un error durante el proceso de guardado
	 * @throws DataIntegrityViolationException si los datos violan restricciones de la base de datos
	 * @see Employee
	 */
	@Transactional
	@Override
	public Employee save(Employee entity) {
		if (entity == null) {
			log.error("Employee entity cannot be null");
	        throw new IllegalArgumentException("Employee entity cannot be null");
	    }
	    if (entity.getId() != null) {
	    	log.error("Cannot save an employee with predefined ID, for updates, use the update method instead.");
	        throw new IllegalArgumentException("Cannot save an employee with predefined ID, for updates, use the update method instead.");
	    }
	    checkForDuplicates(entity);
		return employeeRepository.save(entity);		
	}
	
	/**
	 * Recupera todos los empleados de la base de datos con soporte de paginación y ordenamiento.
	 * 
	 * <p>Este método permite obtener resultados paginados.</p>
	 * 
	 * <p><strong>Ejemplo de uso:</strong></p>
	 * <pre>
	 *     // Obtener la primera página con 10 elementos ordenados por apellido
	 *     Pageable pageable = PageRequest.of(0, 10, Sort.by("lastName").ascending());
	 *     Page&lt;Employee&gt; employeesPage = employeeService.findAll(pageable);
	 * </pre>
	 *
	 * @param pageable Objeto que contiene la información de paginación y ordenamiento:
	 *                 <ul>
	 *                 <li>page: número de página (empezando desde 0)</li>
	 *                 <li>size: cantidad de elementos por página</li>
	 *                 <li>sort: criterios de ordenamiento (campo y dirección)</li>
	 *                 </ul>
	 * @return Objeto Page que contiene los empleados para la página solicitada junto con
	 *         metadatos de paginación como número total de elementos, páginas, etc.
	 * @throws IllegalArgumentException si el parámetro pageable es null
	 * @see org.springframework.data.domain.Pageable
	 * @see org.springframework.data.domain.Page
	 * @see org.springframework.data.domain.Sort
	 */
	@Override
	public Page<Employee> findAll(Pageable pageable) {
		if (pageable == null) {
	        throw new IllegalArgumentException("Pageable parameter cannot be null");
	    }
		return employeeRepository.findAll(pageable);
	}

	/**
	 * Realiza una eliminación lógica (soft delete) de un empleado en el sistema.
	 * 
	 * <p>En lugar de eliminar físicamente el registro de la base de datos, este método
	 * marca al empleado como inactivo estableciendo el campo 'active' en false y
	 * registrando la fecha y hora de eliminación en el campo 'deletedAt'. Esto permite
	 * mantener un historial completo de los empleados y potencialmente restaurarlos
	 * en el futuro si fuera necesario.</p>
	 * 
	 * <p><strong>Comportamiento:</strong></p>
	 * <ul>
	 *   <li>Si el empleado existe y está activo, se marca como inactivo</li>
	 *   <li>Si el empleado no existe, se lanza una excepción ResourceNotFoundException</li>
	 *   <li>Si el empleado ya estaba eliminado (inactivo), se lanza una excepción ServiceException</li>
	 * </ul>
	 *
	 * <p><strong>Ejemplo de uso:</strong></p>
	 * <pre>
	 *     UUID employeeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
	 *     employeeService.delete(employeeId);
	 *     // El empleado ahora estará marcado como inactivo
	 * </pre>
	 *
	 * @param id Identificador único (UUID) del empleado que se desea eliminar
	 * @throws ResourceNotFoundException si no se encuentra ningún empleado con el ID proporcionado
	 * @throws ServiceException si el empleado ya había sido eliminado previamente
	 * @throws IllegalArgumentException si el ID es null
	 * @see Employee
	 * @see ResourceNotFoundException
	 * @see ServiceException
	 */
	@Transactional
	@Override
	public void delete(UUID id) {
		if (id == null) {
	        throw new IllegalArgumentException("Employee ID cannot be null");
	    }
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
		if(!employee.isActive()) {
			log.error("Employee already deleted: " + id);
			throw new ServiceException("Employee already deleted: " + id);
		}
		
		employee.setDeletedAt(LocalDateTime.now());
		employee.setActive(false);
		employeeRepository.save(employee);
	}
	
	/**
	 * Actualiza la información de un empleado existente en el sistema.
	 * 
	 * <p>Este método actualiza únicamente los campos proporcionados en la entidad de entrada,
	 * manteniendo los valores existentes para los campos no especificados. Se registra la fecha
	 * y hora de la última actualización para fines de auditoría. La operación se realiza dentro 
	 * de una transacción para garantizar la integridad de los datos.</p>
	 * 
	 * <p><strong>Proceso de actualización:</strong></p>
	 * <ol>
	 *   <li>Verifica que el empleado exista en la base de datos</li>
	 *   <li>Registra la fecha y hora de actualización</li>
	 *   <li>Actualiza solo los campos proporcionados en la entidad de entrada</li>
	 *   <li>Persiste los cambios en la base de datos</li>
	 * </ol>
	 * 
	 * <p><strong>Campos actualizables:</strong> nombre, apellido, correo electrónico, 
	 * posición, salario, departamento, etc. Los campos de auditoría como 'createdAt', 
	 * 'createdBy', 'deletedAt' y 'deletedBy' no se actualizan a través de este método.</p>
	 * 
	 * <p><strong>Ejemplo de uso:</strong></p>
	 * <pre>
	 *     Employee employeeToUpdate = new Employee();
	 *     employeeToUpdate.setId(existingId);
	 *     employeeToUpdate.setPosition("Senior Java Developer");
	 *     
	 *     Employee updatedEmployee = employeeService.update(employeeToUpdate);
	 *     // Solo la posición será actualizada
	 * </pre>
	 *
	 * @param entity Objeto Employee que contiene el ID del empleado a actualizar y los nuevos valores
	 *               para los campos que se desean modificar. Los campos con valor null no se actualizarán.
	 * @return La entidad Employee actualizada con todos sus campos, tal como queda almacenada en la base de datos
	 * @throws ResourceNotFoundException si no existe un empleado con el ID proporcionado
	 * @throws IllegalArgumentException si la entidad o su ID son null
	 * @throws ServiceException si ocurre un error durante el proceso de actualización
	 * @throws DataIntegrityViolationException si los nuevos datos violan restricciones de integridad 
	 *         (por ejemplo, un dni duplicado)
	 * @see Employee
	 * @see #updateEmployeeFields(Employee, Employee)
	 */
	@Transactional
	@Override
	public Employee update(Employee entity) {
		if (entity == null) {
			log.error("Employee entity cannot be null");
	        throw new IllegalArgumentException("Employee entity cannot be null");
	    }	    
	    if (entity.getId() == null) {
	    	log.error("Employee ID cannot be null for update operation");
	        throw new IllegalArgumentException("Employee ID cannot be null for update operation");
	    }
	    Employee employee;
	    try {
			employee = employeeRepository.findById(entity.getId()).orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + entity.getId()));
			employee.setUpdatedAt(LocalDateTime.now());
			updateEmployeeFields(employee, entity);
			employee = employeeRepository.save(employee);
	    } catch(Exception e) {
	    	log.error("Error to update employee: " + ExceptionUtils.getRootCause(e));
	    	throw new ServiceException("Error to update employee", e);
		}
		return employee;		
	}

	/**
	 * Guarda múltiples empleados en el sistema, procesando cada uno en una transacción independiente.
	 * 
	 * <p>Este método permite realizar operaciones de creación masiva de empleados, donde cada empleado
	 * se procesa individualmente y de manera independiente. Esto significa que si uno o más empleados
	 * no pueden ser guardados debido a errores de validación, duplicados u otras excepciones, el resto
	 * de los empleados válidos se guardarán correctamente.</p>
	 *
	 * @param employees Lista de entidades Employee a guardar. Cada entidad debe contener todos los campos
	 *                 requeridos para crear un nuevo empleado.
	 * @return Lista de EmployeeResultDTO que contiene el resultado de cada operación, incluyendo:
	 *         <ul>
	 *           <li>DNI del empleado</li>
	 *           <li>ID asignado (si se guardó correctamente)</li>
	 *           <li>Código de estado (200 para éxito, 500 para error)</li>
	 *           <li>Mensaje (OK o descripción del error)</li>
	 *         </ul>
	 * @throws IllegalArgumentException si la lista de empleados es null
	 * @see Employee
	 * @see EmployeeResultDTO
	 */
	@Override
	public List<EmployeeResultDTO> save(List<Employee> employees) {
		if (employees == null) {
	        throw new IllegalArgumentException("Employee list cannot be null");
	    }
		if (employees.isEmpty()) {
	        return new ArrayList<>();
		}
		DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);        
		List<EmployeeResultDTO> resultDTO = new ArrayList<>();
		for(int i=0; i<employees.size(); i++) {
			Employee employee = employees.get(i);		
			TransactionStatus txStatus = transactionManager.getTransaction(txDef);
			try {
				checkForDuplicates(employee);
				employeeRepository.saveAndFlush(employee);
				resultDTO.add(new EmployeeResultDTO(employee.getDni(), employee.getId(), 200, "OK"));
				transactionManager.commit(txStatus);
			}catch(Exception e) {
				log.error("Error to save list of employees: " + ExceptionUtils.getRootCause(e));
				transactionManager.rollback(txStatus);
				resultDTO.add(new EmployeeResultDTO(employee.getDni(), null, 500, ExceptionUtils.getRootCauseMessage(e)));
			}
		}
		return resultDTO;
	}
	
	/**
	 * Actualiza solo los campos que deben ser modificados en el empleado existente.
	 * @param existingEmployee Empleado existente en la base de datos
	 * @param updatedEmployee Empleado con los datos actualizados
	 */
	private void updateEmployeeFields(Employee existingEmployee, Employee updatedEmployee) {
	    if (updatedEmployee.getName() != null) {
	        existingEmployee.setName(updatedEmployee.getName());
	    }
	    
	    if (updatedEmployee.getFirstname() != null) {
	        existingEmployee.setFirstname(updatedEmployee.getFirstname());
	    }
	    
	    if (updatedEmployee.getLastname() != null) {
	        existingEmployee.setLastname(updatedEmployee.getLastname());
	    }
	    
	    if (updatedEmployee.getMiddlename() != null) {
	        existingEmployee.setMiddlename(updatedEmployee.getMiddlename());
	    }
	    
	    
	    if (updatedEmployee.getPosition() != null) {
	        existingEmployee.setPosition(updatedEmployee.getPosition());
	    }
	    
	    if (updatedEmployee.getBirthdate() != null) {
	        existingEmployee.setBirthdate(updatedEmployee.getBirthdate());
	    }
	    
	    if (updatedEmployee.getGenre() != null) {
	        existingEmployee.setGenre(updatedEmployee.getGenre());
	    }
	    
	    if (updatedEmployee.getDni() != null) {
	        existingEmployee.setDni(updatedEmployee.getDni());
	    }
	    
	    existingEmployee.setActive(updatedEmployee.isActive());	    
	}
	
	/**
	 * Verifica si ya existe un empleado con el mismo DNI.
	 *
	 * @param employee Empleado a verificar
	 * @throws DuplicateResourceException si ya existe un empleado con el mismo DNI
	 */
	private void checkForDuplicates(Employee employee) {
	    if (employeeRepository.findByDniIgnoreCase(employee.getDni()).isPresent()) {
	        throw new DuplicateResourceException("An employee with DNI '" + employee.getDni() + "' already exists");
	    }
	}

	
}
