package com.example.dscatalog.resources;

import com.example.dscatalog.dto.UserDTO;
import com.example.dscatalog.entities.User;
import com.example.dscatalog.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/user")

public class UserResource {
    @Autowired
    private UserServices services;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> dto =services.findAll();
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value="{/id}")
    public ResponseEntity<UserDTO> findBy(@PathVariable Long id){
        UserDTO dto = services.findBy(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody User user){
        UserDTO  dto=services.insert(user);
        URI uri= ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("{/id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    @PutMapping(value="{/id}")
    public ResponseEntity<UserDTO> update(@RequestBody User user, @PathVariable Long id){
        UserDTO dto = services.update(user,id);
        return ResponseEntity.ok().body(dto);
    }

   @DeleteMapping(value="{/id}")
   public ResponseEntity<Void> delete(@PathVariable Long id){
       services.delete(id);
       return ResponseEntity.noContent().build();
   }
}
