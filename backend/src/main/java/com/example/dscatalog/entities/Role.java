package com.example.dscatalog.entities;

import com.example.dscatalog.dto.RoleDTO;
import org.hibernate.service.spi.InjectService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name="tb_role")

public class Role implements Serializable{
    public static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String authority;

    public Role(){};

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Role(RoleDTO entity) {
        this.authority=entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(authority, role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }
}
