package ru.yandex.practicum.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.*;
import ru.yandex.practicum.account.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoRs> getUserInfoByUsername(String username) {
        UserInfo result = userService.findByUsername(username);
        return ResponseEntity.ok(new UserInfoRs().userInfo(result).responseInfo(new ResponseInfo(true)));
    }

    @Override
    public ResponseEntity<UserInfoRs> registerNewUser(UserRegisterInfo userRegisterInfo) {
        UserInfo result = userService.saveNewUser(userRegisterInfo);
        return ResponseEntity.ok(new UserInfoRs().userInfo(result).responseInfo(new ResponseInfo(true)));
    }

    @Override
    public ResponseEntity<UserInfoRs> updateUserInfo(String username,
                                                     UpdateUserInfoRq updateUserInfoRq) {
        UserInfo result = userService.updateUser(username, updateUserInfoRq);
        return ResponseEntity.ok(new UserInfoRs().userInfo(result).responseInfo(new ResponseInfo(true)));
    }

    @Override
    public ResponseEntity<RegisteredUsersRs> getAllRegisteredUsers() {
        return ResponseEntity.ok(
                new RegisteredUsersRs(new ResponseInfo(true)).users(userService.getAllRegisteredUsers())
        );
    }
}
