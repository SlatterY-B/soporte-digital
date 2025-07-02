package com.digital.controller;


import com.digital.entities.User;
import com.digital.exception.ResourceNotFoundException;
import com.digital.service.UserServiceImpl;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private boolean  isAdmin(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return user.getRole() == User.Role.ADMIN;
    }



//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) {
//
//
//        if(!isAdmin(authentication)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        return ResponseEntity.ok(userService.findAll());
//    }



    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User userSaved = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        User user = userService.findById(id);
        if(user != null) {
            return ResponseEntity.ok(user);
        }else{
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User userUpdated = userService.findById(id);

        userUpdated.setFullName(user.getFullName());

        this.userService.save(userUpdated);
        return ResponseEntity.ok(userUpdated);

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.findById(id);

        if ( user == null ) {
            throw new ResourceNotFoundException("User not found id: " + id);
        }
        userService.delete(user.getId());

        Map <String, Boolean> response = new HashMap<>();

        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/users/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsersPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size, Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.getUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }


    @GetMapping("/agents")
    public ResponseEntity<List<User>> getAllAgents(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<User> agents = userService.findByRole(User.Role.AGENT);
        return ResponseEntity.ok(agents);
    }

    //test








}

















