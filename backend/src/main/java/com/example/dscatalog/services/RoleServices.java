package com.example.dscatalog.services;

import com.example.dscatalog.dto.RoleDTO;
import com.example.dscatalog.entities.Role;
import com.example.dscatalog.repositories.RoleRepository;;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServices {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleDTO> findAll(){
        List<Role> list =repository.findAll();
        return list.stream().map(x->new RoleDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDTO findBy(Long id){
        Optional<Role> role = repository.findById(id);
        return  new RoleDTO(role.orElseThrow(()-> new ResourceNotFoundException(id+"Nao encontrado")));
    }

    @Transactional
    public RoleDTO insert(Role role){
        return new RoleDTO(repository.save(role));
    }

    @Transactional
    public RoleDTO update (Role role, Long id){
        Role roleRecover =new Role(findBy(id));
        roleRecover.setAuthority(role.getAuthority());
        return new RoleDTO(repository.save(roleRecover));
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}
