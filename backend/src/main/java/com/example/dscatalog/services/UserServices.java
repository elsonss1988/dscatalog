package com.example.dscatalog.services;

import com.example.dscatalog.dto.RoleDTO;
import com.example.dscatalog.dto.UserDTO;
import com.example.dscatalog.dto.UserInsertDTO;
import com.example.dscatalog.dto.UserUpdateDTO;
import com.example.dscatalog.entities.Role;
import com.example.dscatalog.entities.User;
import com.example.dscatalog.repositories.RoleRepository;
import com.example.dscatalog.repositories.UserRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServices implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findBy(Long id) {
        Optional<User> user = repository.findById(id);
        return new UserDTO(user.orElseThrow(() -> new ResourceNotFoundException(id + "Nao encontrado")));
    }

    @Transactional
    public UserDTO insert(UserInsertDTO user) {
        User entity = new User();
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        for (RoleDTO role: user.getRoles()) {
            Role roleAdd =roleRepository.getById(role.getId());
            entity.getRoles().add(roleAdd);
        }
        return new UserDTO(repository.save(entity));
    }

    @Transactional
    public UserUpdateDTO update(UserUpdateDTO user, Long id) {
        try {
            User userRecover = new User(findBy(id));
            userRecover.setFirstName(user.getFirstName());
            userRecover.setLastName(user.getLastName());
            userRecover.setEmail(user.getEmail());
            userRecover.getRoles().clear();
            for (RoleDTO roleDTO : user.getRoles()) {
                Role roleAdd = roleRepository.getById(roleDTO.getId());
                userRecover.getRoles().add(roleAdd);
            }
            return new UserUpdateDTO(repository.save(userRecover));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(id + " Nao encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(id + " Associado a algum role");
        }
    }

    public void delete(Long id){
        try {
            repository.deleteById(id);
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(id + " Nao encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(id + " Associado a algum role");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =repository.findByEmail(username);
        if(user == null){
            logger.error("User not found: "+ username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("Üser found:"+username);
        return user;
    }
}
