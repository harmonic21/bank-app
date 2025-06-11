package ru.yandex.practicum.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.account.entity.UserEntity;
import ru.yandex.practicum.account.exceptions.UserNotFoundException;
import ru.yandex.practicum.account.model.UpdateUserInfoRq;
import ru.yandex.practicum.account.model.UserInfo;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.account.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/schema.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("В БД нет пользователя с искомым логином")
    void mustNotFindUser() {
        userRepository.deleteAll();
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByUsername("test")
        );
        assertEquals("Пользователь с именем test не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Успешно сохраняется новый пользователь")
    void mustCreateNewUser() {
        userRepository.deleteAll();

        var user = new UserRegisterInfo()
                .username("test")
                .password("test-pass")
                .addRolesItem("test_user");
        userService.saveNewUser(user);

        UserInfo result = userService.findByUsername("test");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Должен обновить почту, пароль, имя")
    void mustUpdateUser() {
        userRepository.deleteAll();

        UserEntity newUser = new UserEntity();
        newUser.setUsername("test");
        newUser.setPassword("test-pass");
        newUser.setRoles(new String[]{"USER"});

        userRepository.save(newUser);
        UserEntity beforeUpdate = userRepository.findByUsername("test").orElse(null);

        var updateRequest = new UpdateUserInfoRq()
                .email("email")
                .password("new-pass")
                .fullName("full-name");
        userService.updateUser("test", updateRequest);

        UserEntity afterUpdate = userRepository.findByUsername("test").orElse(null);

        assertNotNull(afterUpdate);
        assertEquals("email", afterUpdate.getEmail());
        assertEquals("new-pass", afterUpdate.getPassword());
        assertEquals("full-name", afterUpdate.getFullName());
    }
}