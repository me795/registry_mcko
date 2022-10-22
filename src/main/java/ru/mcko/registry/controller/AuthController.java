package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.service.SecurityService;
import ru.mcko.registry.service.UserService;
import ru.mcko.registry.validator.UserValidator;

@Controller
public class AuthController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;
    private final String scriptsVersion;

    public AuthController(UserService userService,
                          SecurityService securityService,
                          UserValidator userValidator,
                          @Value("${app.scripts.version}") String scriptsVersion) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
        this.scriptsVersion = scriptsVersion;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("scriptsVersion", scriptsVersion);

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        model.addAttribute("scriptsVersion", scriptsVersion);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.create(userForm);
        securityService.autoLogin(userForm.getEmail(), userForm.getConfirmPassword());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        model.addAttribute("scriptsVersion", scriptsVersion);
        return "login";
    }


}
