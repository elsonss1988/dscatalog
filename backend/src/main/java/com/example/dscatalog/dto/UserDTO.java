package com.example.dscatalog.dto;

import com.example.dscatalog.entities.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;

    @NotEmpty(message="Campo Obrigatorio")
    private String firstName;
    private String lastName;

    @Email(message="Favor entrar um email valido")
    private String email;

    Set<RoleDTO> roles = new HashSet<>();

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
