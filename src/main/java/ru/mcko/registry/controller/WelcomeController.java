package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.service.DelegateService;
import ru.mcko.registry.service.OrganizationService;

@Controller
public class WelcomeController {

    private final DelegateService delegateService;
    private final OrganizationService organizationService;
    private final String scriptsVersion;

    public WelcomeController(DelegateService delegateService,
                             OrganizationService organizationService,
                             @Value("${app.scripts.version}") String scriptsVersion) {
        this.delegateService = delegateService;
        this.organizationService = organizationService;
        this.scriptsVersion = scriptsVersion;
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {

        model.addAttribute("allOrganizationsCount", organizationService.countAll());
        model.addAttribute("activeOrganizationsCount", organizationService.count());
        model.addAttribute("allDelegatesCount", delegateService.countAll());
        model.addAttribute("activeDelegatesCount", delegateService.count());

        model.addAttribute("scriptsVersion", scriptsVersion);
        return "welcome";
    }

}
