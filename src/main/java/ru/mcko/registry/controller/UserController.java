package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.service.SecurityService;
import ru.mcko.registry.service.UserService;
import ru.mcko.registry.validator.UserValidator;

@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;
    private final String scriptsVersion;

    public UserController(UserService userService,
                          SecurityService securityService,
                          UserValidator userValidator,
                          @Value("${app.scripts.version}") String scriptsVersion) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
        this.scriptsVersion = scriptsVersion;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(Model model) {

        var users = userService.listAll();
        model.addAttribute("users", users);

        return "users";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable(name = "id") Integer id,
                            Model model) {

        var user = userService.findById(id);
        if (user != null){
            model.addAttribute("user", user);
            return "user_form";
        }else{
            return "error";
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String editUser(Model model) {

        model.addAttribute("user", new User());
            return "user_form";

    }
}
