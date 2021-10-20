package com.example.dscatalog.resources;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping(value="/categories")
public class CategoryResource {

    @Autowired
    private CategoryServices categoryServices;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(
        @RequestParam(value="page", defaultValue= "0") Integer page,
        @RequestParam(value="linesPerPage", defaultValue="12") Integer linesPerPage,
        @RequestParam(value="direction",defaultValue = "ASC") String direction,
        @RequestParam(value="orderBy", defaultValue="name") String orderBy

    ){
        //Part I
        //List<Category> list = new ArrayList<>();
        //list.add(new Category(1L, "Books"));
        //list.add(new Category(2L,"Electronics"));

        //Part II
        //list=categoryServices.findAll();
        //List<CategoryDTO> list = new ArrayList<>();

        //Part III
        PageRequest pageRequest= PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy)  ;
        Page<CategoryDTO> list = categoryServices.findAll(pageRequest);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CategoryDTO> findbyId(@PathVariable Long id){
        CategoryDTO categorydto= categoryServices.findbyId(id);
        return ResponseEntity.ok().body(categorydto);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
            dto=categoryServices.insert(dto);
            URI uri= ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(dto.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO dto, @PathVariable Long id){
        //CategoryDTO catDto=categoryServices.findbyId(id);
        //catDto.setName(dto.getName());
        //dto=categoryServices.update(catDto);
        dto=categoryServices.update(dto,id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value="/{id}")

    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
