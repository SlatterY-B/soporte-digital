package com.digital.service;

import com.digital.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {


    List<User> findAll();

    User findById(Long id);

    User save(User user);

    void delete(Long id);

    User findByEmail(String email);

    Page<User> getUsers(Pageable pageable);

}
