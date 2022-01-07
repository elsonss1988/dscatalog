package com.example.dscatalog.services;

import com.example.dscatalog.dto.DepartmentDTO;
import com.example.dscatalog.entities.Department;
import com.example.dscatalog.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServices {
    @Autowired
    private DepartmentRepository repository;

    public List<DepartmentDTO> findAll(){
        List<Department> departmentList = repository.findAll(Sort.by("name"));
        List<DepartmentDTO> list= departmentList.stream().map(x->new DepartmentDTO(x)).collect(Collectors.toList());
        return list;
    }
}
