package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    boolean checkIsUserEmailExists(String email);
    boolean checkUserIsExists(Long userId);
    User create(User user);
    User edit(Long id, User user);
    void delete(Long id);
    User getUser(Long id);
    List<User> getAllUsers();
}
