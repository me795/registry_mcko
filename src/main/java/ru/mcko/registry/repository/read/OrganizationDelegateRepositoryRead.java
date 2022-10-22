package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.OrganizationDelegate;

import java.util.List;

@Repository
public interface OrganizationDelegateRepositoryRead {

    List<OrganizationDelegate> listByOrganization(Integer organizationId);

    List<OrganizationDelegate> listActiveByOrganization(Integer organizationId);

    OrganizationDelegate getById(@Param("id") Integer id);
}
