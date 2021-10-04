package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repository.CategoryRepository;
import com.example.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServices {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list=categoryRepository.findAll();
        List<CategoryDTO> listDto= list.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
        return listDto;
    }

    @Transactional(readOnly=true)
    public CategoryDTO findbyId(Long id){
        Optional <Category> category =categoryRepository.findById(id);
        CategoryDTO dto= new CategoryDTO(category.orElseThrow(()->new EntityNotFoundException("Entity Not Found")));
        return dto;
    }

}
