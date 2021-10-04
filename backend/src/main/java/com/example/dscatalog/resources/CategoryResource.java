package com.example.dscatalog.resources;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping(value="/categories")
public class CategoryResource {

    @Autowired
    private CategoryServices categoryServices;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){
        //List<Category> list = new ArrayList<>();
        //list.add(new Category(1L, "Books"));
        //list.add(new Category(2L,"Electronics"));
        List<CategoryDTO> list = new ArrayList<>();
        list=categoryServices.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CategoryDTO> findbyId(@PathVariable Long id){
        CategoryDTO categorydto= categoryServices.findbyId(id);
        return ResponseEntity.ok().body(categorydto);
    }
}
