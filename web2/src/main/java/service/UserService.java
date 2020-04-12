package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class UserService {
    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> getAllUsers() {
        return dataBase.isEmpty() ? new ArrayList<>() : new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return false;
        }
        if (!dataBase.containsValue(user)) {
            dataBase.put(maxId.longValue(), new User(maxId.longValue(), user.getEmail(), user.getPassword()));
            maxId.getAndIncrement();
            return true;
        }
        return false;
    }

    public void deleteAllUser() {
        authMap.clear();
        dataBase.clear();
        maxId.set(0);
    }

    public boolean isExistsThisUser(User user) {
        return dataBase.containsValue(user);
    }

    public List<User> getAllAuth() {
        return authMap.isEmpty() ? new ArrayList<>() : new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        if (dataBase.isEmpty()) {
            return false;
        }
        if (dataBase.containsValue(user)) {
            Collection<User> users = dataBase.values();
            for (User value : users) {
                if (value.equals(user) && value.getPassword().compareTo(user.getPassword()) == 0) {
                    authMap.put(value.getId(), dataBase.get(value.getId()));
                    return true;
                }
            }
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

}
