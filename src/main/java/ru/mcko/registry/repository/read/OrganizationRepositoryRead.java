package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.Organization;

import java.util.List;
import java.util.Map;

@Repository
public interface OrganizationRepositoryRead {

    List<Organization> list(@Param("params") Map<String,Object> params);

    List<Organization> listByName(String searchValue);
    List<Organization> listFull(@Param("params") Map<String,Object> params);

    Long count();

    Long countAll();

    Long countWithFilter(@Param("params") Map<String,Object> params);

    Organization getById(@Param("id") Integer id);

}
