package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mcko.registry.entity.Organization;
import ru.mcko.registry.service.DelegateService;
import ru.mcko.registry.service.DocumentTypeService;
import ru.mcko.registry.service.OrganizationService;

import java.util.Date;
import java.util.HashMap;

@Controller
public class OrganizationController {

    private final OrganizationService organizationService;
    private final DelegateService delegateService;
    private final DocumentTypeService documentTypeService;
    private final String scriptsVersion;

    public OrganizationController(OrganizationService organizationService, DelegateService delegateService, DocumentTypeService documentTypeService, @Value("${app.scripts.version}") String scriptsVersion) {
        this.organizationService = organizationService;
        this.delegateService = delegateService;
        this.documentTypeService = documentTypeService;
        this.scriptsVersion = scriptsVersion;
    }


    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public String organizations(Model model) {

        return "organizations";
    }

    @RequestMapping(value = "/organization/{id}", method = RequestMethod.GET)
    public String editOrganization(@PathVariable(name = "id") Integer id,
                                   Model model) {

        var organization = organizationService.getById(id);
        if (organization != null) {
            model.addAttribute("organization", organization);
            model.addAttribute("docTypes", documentTypeService.listAll());
            return "organization_form";
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/organization", method = RequestMethod.GET)
    public String editDelegate(Model model) {

        model.addAttribute("organization", new Organization());
        model.addAttribute("docTypes", documentTypeService.listAll());
        return "organization_form";

    }

    @RequestMapping(value = "/registry/organization", method = RequestMethod.POST)
    public String viewRegistryOrganization(@RequestParam(value = "delegateId", required = false) Integer delegateId,
                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                           @RequestParam(value = "organizationId", required = false) Integer organizationId,
                                           Model model) {

        var params = new HashMap<String, Object>();
        if (delegateId != null) {
            params.put("delegateId", delegateId);
            model.addAttribute("selectedDelegate", delegateService.getById(delegateId));
        }
        if (date != null) {
            params.put("date", date);
            model.addAttribute("date", date);
        }
        if (organizationId != null) {
            params.put("organizationId", organizationId);
            model.addAttribute("selectedOrganization", organizationService.getById(organizationId));
        }
        if ((organizationId != null) || (date != null) || (delegateId != null)) {
            model.addAttribute("organizationList", organizationService.listFull(params));
        }

        return "registry_organization";

    }

    @RequestMapping(value = "/registry/organization", method = RequestMethod.GET)
    public String viewRegistryOrganization(Model model) {

        return "registry_organization";

    }
}
