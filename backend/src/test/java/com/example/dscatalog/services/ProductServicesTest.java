package com.example.dscatalog.services;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.factories.Factory;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.ProductRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServicesTest {

    @InjectMocks
    private ProductServices service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() throws Exception {
        existingId= 1L;
        nonExistingId= 2L;
        dependentId= 4L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public  void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(()->{
            service.delete(existingId);
        });

        Mockito.verify(repository,Mockito.times(1)).deleteById(existingId);
    }


    @Test
    public  void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.delete(nonExistingId);
        });

        Mockito.verify(repository,Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public  void deleteShouldThrowDataIntegrityViolationExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(DataBaseException.class,()->{
            service.delete(dependentId);
        });

        Mockito.verify(repository,Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPageShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAll(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExisting(){
       ProductDTO result =service.findById(existingId);
       Assertions.assertNotNull(result);
       Mockito.verify(repository,Mockito.times(1)).findById(existingId);
    }


    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionDtoWhenIdNotExisting(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.findById(nonExistingId);
        });

        Mockito.verify(repository,Mockito.times(1)).findById(nonExistingId);
    }
    @Test
    public void updateShouldReturnProductDtoWhenIdExisting(){

        ProductDTO result =service.update(productDTO, existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(repository,Mockito.times(1)).getOne(existingId);
    }

    @Test
    public void updateIdShouldThrowResourceNotFoundExceptionDtoWhenIdNotExisting(){
        Assertions.assertThrows(EntityNotFoundException.class,()->{
            service.update(productDTO,nonExistingId);
        });

        Mockito.verify(repository,Mockito.times(1)).getOne(nonExistingId);
    }
}
