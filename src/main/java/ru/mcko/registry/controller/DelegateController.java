package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.service.DelegateService;

@Controller
public class DelegateController {

    private final DelegateService delegateService;
    private final String scriptsVersion;

    public DelegateController(DelegateService delegateService, @Value("${app.scripts.version}") String scriptsVersion) {
        this.delegateService = delegateService;
        this.scriptsVersion = scriptsVersion;
    }


    @RequestMapping(value = "/delegates", method = RequestMethod.GET)
    public String delegates(Model model) {

        return "delegates";
    }

    @RequestMapping(value = "/delegate/{id}", method = RequestMethod.GET)
    public String editDelegate(@PathVariable(name = "id") Integer id,
                            Model model) {

        var delegate = delegateService.getById(id);
        if (delegate != null){
            model.addAttribute("delegate", delegate);
            return "delegate_form";
        }else{
            return "error";
        }
    }

    @RequestMapping(value = "/delegate", method = RequestMethod.GET)
    public String editDelegate(Model model) {

        model.addAttribute("delegate", new Delegate());
            return "delegate_form";

    }
}
