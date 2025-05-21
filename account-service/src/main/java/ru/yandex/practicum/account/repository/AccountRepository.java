package ru.yandex.practicum.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.account.entity.UserAccountEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<UserAccountEntity, UUID> {

    @Query("""
            SELECT account
            FROM UserAccountEntity account
            WHERE account.user.username = :username""")
    List<UserAccountEntity> findAllUserAccounts(@Param("username") String username);

    @Query("""
            SELECT account
            FROM UserAccountEntity account
            WHERE account.user.username = :username AND account.currency = :currency""")
    Optional<UserAccountEntity> findUserAccountWithCurrency(@Param("username") String username, @Param("currency") String currency);
}
