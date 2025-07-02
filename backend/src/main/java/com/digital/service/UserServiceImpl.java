package com.digital.service;

import com.digital.entities.Notification;
import com.digital.entities.User;
import com.digital.entities.repository.NotificationRepository;
import com.digital.entities.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User save(User user) {

        boolean isNew = user.getId() == null;

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);

        if (isNew) {
            List<User> admins = userRepository.findByRole(User.Role.ADMIN);
            for (User admin : admins) {
                notificationService.createAndSaveNotification(
                        "New user registered",
                        "A new user has registered: " + savedUser.getFullName() + " (" + savedUser.getEmail() + ")",
                        admin,
                        Notification.NotificationType.ADMIN
                );
            }
        }
        return savedUser;
    }

    @Override
    public void delete(Long id) {

        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    //@Override
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return userRepository.findAll(pageable);
    }

    public List<User> findByRole(User.Role role) {
        return userRepository.findByRole(role);
    }


}
