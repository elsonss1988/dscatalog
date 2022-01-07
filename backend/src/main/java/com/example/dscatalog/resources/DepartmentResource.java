package com.example.dscatalog.resources;

import com.example.dscatalog.dto.DepartmentDTO;
import com.example.dscatalog.services.DepartmentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/departments")
public class DepartmentResource {

    @Autowired
    private DepartmentServices departmentServices;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> findAll(){
        List<DepartmentDTO> list = departmentServices.findAll();

        return ResponseEntity.ok().body(list);
    }

}
