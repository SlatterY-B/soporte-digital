package com.digital.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    private String email;

    @Column(name = "password_user")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_user", nullable = false)
    private Role role;

    public enum Role {
        CUSTOMER,
        AGENT,
        ADMIN
    }

}
