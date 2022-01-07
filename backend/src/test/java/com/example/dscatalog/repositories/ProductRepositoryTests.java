package com.example.dscatalog.repositories;


import com.example.dscatalog.factories.Factory;
import com.example.dscatalog.entities.Product;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;


@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;
    long existingId;
    long nonExistingId;
    long countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 0l;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNUll(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts+1,product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectsWhenIdExists(){
        repository.deleteById(existingId);
            Optional<Product> result =repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExisting(){
        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void  findByIdShouldReturnNotEmptyWhenIdExisting(){
        Optional<Product> result=repository.findById(existingId);
        Assert.assertNotEquals(result,Optional.empty());
    }

    @Test
    public void  findByIdShouldReturnEmptyWhenIdNotExisting(){
        Optional<Product> result=repository.findById(nonExistingId);
        Assert.assertEquals(result,Optional.empty());
    }



}
