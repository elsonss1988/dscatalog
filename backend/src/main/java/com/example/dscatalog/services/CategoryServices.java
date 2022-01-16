package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryServices {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(PageRequest pageRequest){
        Page<Category> list=categoryRepository.findAll(pageRequest);
        Page<CategoryDTO> listDto= list.map(x->new CategoryDTO(x));
        return listDto;
    }

    @Transactional(readOnly=true)
    public CategoryDTO findbyId(Long id){
        Optional <Category> category =categoryRepository.findById(id);
        CategoryDTO dto= new CategoryDTO(category.orElseThrow(()->new ResourceNotFoundException("Entity Not Found")));
        return dto;
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO catDto){
        Category cat = new Category();
        cat.setName(catDto.getName());
        cat=categoryRepository.save(cat);
        return new CategoryDTO(cat);
    }

    @Transactional
    public CategoryDTO update(CategoryDTO dto, Long id){

        try {
            Category cat =  categoryRepository.getOne(id);
            cat.setName(dto.getName());
            cat=categoryRepository.save(cat);
            return new CategoryDTO(cat);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException(" Not Found "+id);
        }catch(DataIntegrityViolationException e){
            throw  new ResourceNotFoundException("");
        }

    }

     public void delete(Long id){
        try {
            categoryRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " +id);
        }catch(DataIntegrityViolationException e){
            throw new DataBaseException("Integrity Violation");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
