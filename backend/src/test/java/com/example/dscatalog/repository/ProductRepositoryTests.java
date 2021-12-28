package com.example.dscatalog.repository;


import com.example.dscatalog.entities.Product;
import com.example.dscatalog.services.ProductServices;
import org.junit.Test;


import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectsWhenIdExists(){
        long existingId = 1L;
        repository.deleteById(existingId);
            Optional<Product> result =repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExisting(){
        long nonExistingId = 0l;
        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            repository.deleteById(nonExistingId);
        });
    }



}
