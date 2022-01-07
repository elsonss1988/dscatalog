package com.example.dscatalog.resources;

import com.example.dscatalog.dto.EmployeeDTO;
import com.example.dscatalog.entities.Employee;
import com.example.dscatalog.services.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/employees")
public class EmployeeResource {

    @Autowired
    private EmployeeServices employeeServices;

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> findAll(
//            @RequestParam(value="page", defaultValue="0") Integer page,
//            @RequestParam(value="linesPerPage", defaultValue="0") Integer linesPerPage,
//            @RequestParam(value="direction", defaultValue="ASC") Integer direction,
//            @RequestParam(value="orderBy", defaultValue="name") Integer orderBy ;
            Pageable pageable
    ){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by("name"));
        Page<EmployeeDTO> employee = employeeServices.findAll(pageRequest);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> insert(
            @RequestBody EmployeeDTO employeeDTO
    ){
       employeeDTO = employeeServices.insert(employeeDTO);
       URI uri= ServletUriComponentsBuilder
               .fromCurrentRequestUri()
               .path("/{id}")
               .buildAndExpand(employeeDTO.getId())
               .toUri();
       return ResponseEntity.created(uri).body(employeeDTO);
    }
}
