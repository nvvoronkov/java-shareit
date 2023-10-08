package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.interfaces.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("userStorage")
@AllArgsConstructor
public class UserStorage implements Storage<User> {
    private final Map<Long, User> mapOfUsers;
    private static Long userId = 0L;

    @Override
    public User add(User user) {
        System.out.println("Storage");
        Long userId = generateUserid();
        user.setId(userId);
        mapOfUsers.put(userId, user);
        return user;
    }

    @Override
    public User update(User user) {
        mapOfUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Long userId) {
        return mapOfUsers.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(mapOfUsers.values());
    }

    public String delete(Long userId) {
        mapOfUsers.remove(userId);
        return String.format("Пользователь id=%s успешно удален", userId);
    }

    private Long generateUserid() {
        return ++userId;
    }

    public Boolean checkIsObjectInStorage(Long userId) {
        return mapOfUsers.containsKey(userId);
    }

    public Boolean checkIsObjectInStorage(User user) {
        return mapOfUsers.containsKey(user.getId());
    }

    public Boolean checkIsUserEmailInStorage(User user) {
        return mapOfUsers.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()));
    }

    public Boolean checkIsUserEmailInStorage(String email) {
        return mapOfUsers.values().stream()
                .map(User::getEmail)
                .anyMatch(emailFromStorage -> emailFromStorage.equals(email));
    }

    public Boolean checkIsUserEmailInStorage(String email, Long userId) {
        return mapOfUsers.values().stream()
                .filter(user -> !(user.getId().equals(userId)))
                .map(User::getEmail)
                .anyMatch(emailFromStorage -> emailFromStorage.equals(email));
    }
}
