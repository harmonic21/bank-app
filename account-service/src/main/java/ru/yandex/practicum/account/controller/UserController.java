package ru.yandex.practicum.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UpdateUserInfoRq;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.account.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoRs> getUserInfoByUsername(String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Override
    public ResponseEntity<UserInfoRs> registerNewUser(UserRegisterInfo userRegisterInfo) {
        return ResponseEntity.ok(userService.saveNewUser(userRegisterInfo));
    }

    @Override
    public ResponseEntity<UserInfoRs> updateUserInfo(String username,
                                                     UpdateUserInfoRq updateUserInfoRq) {
        return ResponseEntity.ok(userService.updateUser(username, updateUserInfoRq));
    }
}
