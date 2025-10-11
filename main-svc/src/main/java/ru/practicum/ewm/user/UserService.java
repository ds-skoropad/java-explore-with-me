package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUserById(Long userId);
}
