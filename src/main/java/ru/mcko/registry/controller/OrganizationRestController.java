package ru.mcko.registry.controller;

import org.springframework.web.bind.annotation.*;
import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.Organization;
import ru.mcko.registry.service.OrganizationService;

import java.util.List;

@RestController
public class OrganizationRestController {

    private final OrganizationService organizationService;

    public OrganizationRestController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/organizations/data")
    public DataTablesOutput<Organization> listDelegates(@RequestBody DataTablesInput dataTablesInput) {

        return organizationService.listForDataTable(dataTablesInput);
    }

    @PostMapping("/organizations/search")
    public List<Organization> searchOrganizations(@RequestParam("searchValue") String searchValue) {

        return organizationService.listByName(searchValue);
    }


    @PutMapping("/organization")
    public Organization createDelegate(@RequestBody Organization organization) {

        var organizationId = organizationService.create(organization);
        organization.setId(organizationId);

        return organization;
    }

    @GetMapping("/organization/{id}/info")
    public Organization getOrganizationInfoById(@PathVariable("id") Integer id) {

        return organizationService.getById(id);
    }


    @PutMapping("/organization/{id}")
    public Organization updateDelegate(@PathVariable("id") Integer id,
                       @RequestBody Organization organization) {
        organization.setId(id);
        organizationService.update(organization);

        return organization;
    }

    @DeleteMapping("/organization/{id}")
    public Organization deleteDelegate(@PathVariable("id") Integer id) {

        var organization = organizationService.getById(id);
        if (organization != null) {
            organizationService.delete(id);
        }

        return organization;
    }




}
