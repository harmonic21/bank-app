package ru.yandex.practicum.account.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "birthday")
    private LocalDate birthDay;
    @Column(name = "email")
    private String email;
    @Column(name = "roles", nullable = false)
    private String[] roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserAccountEntity> accounts = new HashSet<>();

    public void addAccount(UserAccountEntity userAccountEntity) {
        accounts.add(userAccountEntity);
    }
}
