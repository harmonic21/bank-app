package ru.yandex.practicum.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.account.exceptions.UserNotFoundException;
import ru.yandex.practicum.account.mapper.UserInfoMapper;
import ru.yandex.practicum.account.model.ShortUserInfo;
import ru.yandex.practicum.account.model.UpdateUserInfoRq;
import ru.yandex.practicum.account.model.UserInfo;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.account.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;

    public UserInfo findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userInfoMapper::mapToUserInfo)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional
    public UserInfo saveNewUser(UserRegisterInfo userInfo) {
        var newUser = userRepository.save(userInfoMapper.mapToEntity(userInfo));
        return userInfoMapper.mapToUserInfo(newUser);
    }

    @Transactional
    public UserInfo updateUser(String userName, UpdateUserInfoRq updateRequest) {
        userRepository.findByUsername(userName)
                .ifPresentOrElse(
                        userEntity -> {
                            Optional.ofNullable(updateRequest.getFullName()).ifPresent(userEntity::setFullName);
                            Optional.ofNullable(updateRequest.getPassword()).ifPresent(userEntity::setPassword);
                            Optional.ofNullable(updateRequest.getBirthDay()).ifPresent(userEntity::setBirthDay);
                            Optional.ofNullable(updateRequest.getEmail()).ifPresent(userEntity::setEmail);
                            userRepository.save(userEntity);
                        },
                        () -> {throw new UserNotFoundException(userName);}
                );
        return findByUsername(userName);
    }

    public List<ShortUserInfo> getAllRegisteredUsers() {
        return userRepository.findAll().stream()
                .map(user -> new ShortUserInfo(user.getUsername(), user.getFullName()))
                .toList();
    }
}
