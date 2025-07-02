package com.digital.controller;

import com.digital.dto.LoginRequest;
import com.digital.entities.User;
import com.digital.entities.repository.NotificationRepository;
import com.digital.entities.repository.UserRepository;
import com.digital.security.JwtService;
import com.digital.service.NotificationServiceImpl;
import com.digital.service.UserService;
import com.digital.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(value = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @Autowired
    private UserServiceImpl userService;


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//
//        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
//
//        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
//            throw new RuntimeException("Wrong password");
//        }
//
//        String token = jwtService.generateToken(user.getEmail());
//
//        return ResponseEntity.ok().body(Map.of("token", token));
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getEmail());


        return ResponseEntity.ok().body(Map.of(
                "token", token,
                "role", user.getRole().name()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        user.setRole(User.Role.CUSTOMER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        User admin = userService.findByEmail("flysoloxd@gmail.com");

//        if there were many admins
//        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
//        for (User admin : admins) {
//            notificationService.createAndSaveNotification(...);
//        }
//

        notificationServiceImpl.createAndSaveNotification(
                " ✅New User Registration",
                "A new user # " + savedUser.getId() +" has registered with email: " + savedUser.getEmail(),
                admin,
                com.digital.entities.Notification.NotificationType.ADMIN
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "id", savedUser.getId(),
                        "email", savedUser.getEmail(),
                        "fullName", savedUser.getFullName(),
                        "role", savedUser.getRole().name()
                )
        );

    }

}
