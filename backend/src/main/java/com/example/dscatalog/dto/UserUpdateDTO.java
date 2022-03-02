package com.example.dscatalog.dto;

import com.example.dscatalog.entities.User;
import com.example.dscatalog.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO{
    private String password;

    public UserUpdateDTO(){
        super();
    }

    public UserUpdateDTO(User entity) {super();}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

