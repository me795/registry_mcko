package ru.mcko.registry.service;

import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.OrganizationDelegate;

import java.util.List;

public interface OrganizationDelegateService {

    List<OrganizationDelegate> listByOrganization(Integer organizationId);

    List<OrganizationDelegate> listActiveByOrganization(Integer organizationId);

    OrganizationDelegate getById(Integer organizationDelegateId);

    Integer create(OrganizationDelegate organizationDelegate);

    void update(OrganizationDelegate organizationDelegate);

    void delete(Integer organizationDelegateId);

    void deleteAllByOrganization(Integer organizationId);

}
