package com.example.dscatalog.dto;

import com.example.dscatalog.entities.Department;
import com.example.dscatalog.entities.Employee;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


public class DepartmentDTO {

	public Long id;
	public String name;

	public DepartmentDTO() {
	}

	public DepartmentDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public DepartmentDTO(Department department){
		this.setId(department.getId());
		this.setName(department.getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
