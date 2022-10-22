package ru.mcko.registry.service;

import ru.mcko.registry.entity.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    User findByUsername(String username);

    User findActiveByUsername(String username);

    User findById(Integer id);

    List<User> listAll();
}
