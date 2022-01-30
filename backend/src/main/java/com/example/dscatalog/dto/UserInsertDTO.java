package com.example.dscatalog.dto;


public class UserInsertDTO {

    private String password;

    public UserInsertDTO(String password){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
