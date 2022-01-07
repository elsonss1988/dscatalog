package com.example.dscatalog.services;

import com.example.dscatalog.dto.EmployeeDTO;
import com.example.dscatalog.entities.Department;
import com.example.dscatalog.entities.Employee;
import com.example.dscatalog.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EmployeeServices {

    @Autowired
    private EmployeeRepository repository;

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable){
        Page<Employee> employeeList = repository.findAll(pageable);
        Page<EmployeeDTO> page= employeeList.map(x-> new EmployeeDTO(x));
        return page;
    }

    @Transactional
    public EmployeeDTO insert(EmployeeDTO employeeDTO) {
        Employee employee= new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(new Department(employeeDTO.getDepartmentId(),null));
        employee=repository.save(employee);
        return new EmployeeDTO(employee);
    }
}
