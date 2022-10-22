package ru.mcko.registry.repository.write;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.OrganizationDelegate;
import ru.mcko.registry.entity.Stamp;

@Repository
public interface OrganizationDelegateRepositoryWrite {

    Integer create(OrganizationDelegate organizationDelegate);

    void update(OrganizationDelegate organizationDelegate);

    void delete(Integer organizationDelegateId);

    void deleteAllByOrganization(Integer organizationId);

    void addDocumentType(Integer organizationDelegateId, Integer documentTypeId);

    void deleteDocumentType(Integer organizationDelegateId, Integer documentTypeId);

    void deleteAllDocumentType(Integer organizationDelegateId);

}
