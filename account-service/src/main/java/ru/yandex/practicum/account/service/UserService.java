package ru.yandex.practicum.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.exceptions.UserNotFoundException;
import ru.yandex.practicum.account.mapper.UserInfoMapper;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.account.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;

    public UserInfoRs findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userInfoMapper::mapToUserInfo)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserInfoRs saveNewUser(UserRegisterInfo userInfo) {
        var newUser = userRepository.save(userInfoMapper.mapToEntity(userInfo));
        return userInfoMapper.mapToUserInfo(newUser);
    }
}
