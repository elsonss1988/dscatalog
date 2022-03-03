package com.example.dscatalog.resources;

import com.example.dscatalog.dto.UserDTO;
import com.example.dscatalog.dto.UserInsertDTO;
import com.example.dscatalog.dto.UserUpdateDTO;
import com.example.dscatalog.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value="/users")

public class UserResource {

    public static Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    private UserServices services;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> dto =services.findAll();
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findBy(@PathVariable Long id){
        logger.info("ïd"+id);
        UserDTO dto = services.findBy(id);
        return ResponseEntity.ok().body(dto);
    }



    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO user){
        UserDTO  dto=services.insert(user);
        URI uri= ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("{/id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    @PutMapping(value="/{id}")
    public ResponseEntity<UserUpdateDTO> update(@Valid @RequestBody UserUpdateDTO user, @PathVariable Long id){
        UserUpdateDTO dto = services.update(user,id);
        return ResponseEntity.ok().body(dto);
    }

   @DeleteMapping(value="/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id){
       services.delete(id);
       return ResponseEntity.noContent().build();
   }
}
