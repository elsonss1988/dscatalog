package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repository.CategoryRepository;
import com.example.dscatalog.repository.ProductRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductServices {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> list=productRepository.findAll(pageable);
        Page<ProductDTO> listDto= list.map(x->new ProductDTO(x));
        return listDto;
    }

    @Transactional(readOnly=true)
    public ProductDTO findbyId(Long id){
        Optional <Product> product =productRepository.findById(id);
        Product entity= product.orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product prod = new Product();
        copyDtoEntity(dto, prod);
        prod=productRepository.save(prod);
        return new ProductDTO(prod);
    }

    @Transactional
    public ProductDTO update(ProductDTO dto, Long id){

        try {
            Product prod =  productRepository.getOne(id);
            copyDtoEntity(dto,prod);
            prod=productRepository.save(prod);
            return new ProductDTO(prod);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException(" Not Found "+id);
        }catch(DataIntegrityViolationException e){
            throw  new ResourceNotFoundException("");
        }

    }

    private void copyDtoEntity(ProductDTO dto, Product prod) {
        prod.setName(dto.getName());
        prod.setDate(dto.getDate());
        prod.setDescription(dto.getDescription());
        prod.setImgUrl(dto.getImgUrl());
        prod.setPrice(dto.getPrice());

        prod.getCategories().clear();
        for(CategoryDTO catDto: dto.getCategories() ){
            try {
                Category category = categoryRepository.getOne(catDto.getId());
                prod.getCategories().add(category);
            }catch(ResourceNotFoundException e){
                throw new ResourceNotFoundException(" Not Found "+catDto.getId());
            }
        }
    }

    public void delete(Long id){
        try {
            productRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " +id);
        }catch(DataIntegrityViolationException e){
            throw new DataBaseException("Integrity Violation");
        }
    }

}
