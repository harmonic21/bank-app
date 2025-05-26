package ru.yandex.practicum.account.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.account.entity.UserEntity;
import ru.yandex.practicum.account.model.UserInfo;
import ru.yandex.practicum.account.model.UserRegisterInfo;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserInfoMapper {

    UserInfo mapToUserInfo(UserEntity source);
    UserEntity mapToEntity(UserRegisterInfo source);
}
