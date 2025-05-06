package ru.yandex.practicum.account.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "BANK_USER")
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "roles", nullable = false)
    private String[] roles;
}
