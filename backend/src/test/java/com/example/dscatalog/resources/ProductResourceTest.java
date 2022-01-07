package com.example.dscatalog.resources;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.factories.Factory;
import com.example.dscatalog.services.ProductServices;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;


@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServices services;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L ;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page =  new PageImpl<>(List.of(productDTO));

        when(services.findAll(any())).thenReturn(page);

        when(services.findById(existingId)).thenReturn(productDTO);
        when(services.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(services.update(any(), eq(existingId))).thenReturn(productDTO);
        when(services.update(any(), eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        when(services.insert(any())).thenReturn(productDTO);

        doNothing().when(services).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(services).delete(nonExistingId);
        doThrow(DataBaseException.class).when(services).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{

        ResultActions result=mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdExists() throws Exception{
            ResultActions result=mockMvc.perform(get("/products/{id}", nonExistingId)
                    .accept(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }
    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}",nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDeleteProductWhenIdIsValid() throws Exception {

        ResultActions result=mockMvc.perform(delete("/products/{id}", existingId));

        result.andExpect(status().isNoContent());
    }


    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdIsNotFound() throws Exception {

        ResultActions result=mockMvc.perform(delete("/products/{id}", nonExistingId));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenIdIsInvalid() throws Exception {

        ResultActions result=mockMvc.perform(delete("/products/{id}", dependentId));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void insertShouldInsertProductDTOCreated() throws Exception {
        String jsonBody=objectMapper.writeValueAsString(productDTO);

        ResultActions result=
                mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );


        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

}
