package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.service.SecurityService;
import ru.mcko.registry.service.UserService;
import ru.mcko.registry.validator.UserValidator;

@RestController
public class UserRestController {

    private final UserService userService;
    private final UserValidator userValidator;

    public UserRestController(UserService userService,
                          UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


    @PutMapping("/user")
    public User createUser(@RequestBody User user) {

        var createdUser = userService.create(user);

        return createdUser;
    }


    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable("id") Integer id,
                       @RequestBody User user) {
        user.setId(id);
        userService.update(user);

        return user;
    }

    @PutMapping("/user/{id}/status_change")
    public User changeUserStatus(@PathVariable("id") Integer id) {
        var user = userService.findById(id);
        var isBlocked = (user.isBlocked()) ? false : true;
        user.setBlocked(isBlocked);;
        userService.update(user);

        return user;
    }


}
