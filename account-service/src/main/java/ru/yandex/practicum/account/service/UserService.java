package ru.yandex.practicum.account.service;

import io.micrometer.tracing.Tracer;
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
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final Tracer tracer;

    public UserInfo findByUsername(String username) {
        return callDbAndTraceIt(() -> userRepository.findByUsername(username), "user_repository.find_by_username")
                .map(userInfoMapper::mapToUserInfo)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional
    public UserInfo saveNewUser(UserRegisterInfo userInfo) {
        var entityToSave = userInfoMapper.mapToEntity(userInfo);
        var newUser = callDbAndTraceIt(() -> userRepository.save(entityToSave), "user_repository.save");
        return userInfoMapper.mapToUserInfo(newUser);
    }

    @Transactional
    public UserInfo updateUser(String userName, UpdateUserInfoRq updateRequest) {
        callDbAndTraceIt(() -> userRepository.findByUsername(userName), "user_repository.find_by_username")
                .ifPresentOrElse(
                        userEntity -> {
                            Optional.ofNullable(updateRequest.getFullName()).ifPresent(userEntity::setFullName);
                            Optional.ofNullable(updateRequest.getPassword()).ifPresent(userEntity::setPassword);
                            Optional.ofNullable(updateRequest.getBirthDay()).ifPresent(userEntity::setBirthDay);
                            Optional.ofNullable(updateRequest.getEmail()).ifPresent(userEntity::setEmail);
                            callDbAndTraceIt(() -> userRepository.save(userEntity), "user_repository.save");
                        },
                        () -> {throw new UserNotFoundException(userName);}
                );
        return findByUsername(userName);
    }

    public List<ShortUserInfo> getAllRegisteredUsers() {
        return callDbAndTraceIt(userRepository::findAll, "user_repository.find_all").stream()
                .map(user -> new ShortUserInfo(user.getUsername(), user.getFullName()))
                .toList();
    }

    private <T> T callDbAndTraceIt(Supplier<T> runnable, String traceName) {
        var span = tracer.nextSpan().remoteServiceName("postgre-db").name(traceName).start();
        try {
            return runnable.get();
        } finally {
            span.end();
        }
    }
}
