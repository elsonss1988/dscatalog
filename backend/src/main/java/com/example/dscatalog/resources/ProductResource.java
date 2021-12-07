package com.example.dscatalog.resources;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value="/products")
public class ProductResource {

    @Autowired
    private ProductServices productServices;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable ){
        Page<ProductDTO> list = productServices.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ProductDTO> findbyId(@PathVariable Long id){
        ProductDTO productdto= productServices.findbyId(id);
        return ResponseEntity.ok().body(productdto);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
            dto=productServices.insert(dto);
            URI uri= ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(dto.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO dto, @PathVariable Long id){
        //ProductDTO catDto=productServices.findbyId(id);
        //catDto.setName(dto.getName());
        //dto=productServices.update(catDto);
        dto=productServices.update(dto,id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value="/{id}")

    public ResponseEntity<Void> delete(@PathVariable Long id){
        productServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
