package ru.mcko.registry.repository.write;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Organization;

@Repository
public interface OrganizationRepositoryWrite {

    Integer create(Organization organization);

    void update(Organization organization);

    void delete(Integer organizationId);

}
