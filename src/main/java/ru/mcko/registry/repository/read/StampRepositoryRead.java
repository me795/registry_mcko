package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Stamp;

import java.util.List;

@Repository
public interface StampRepositoryRead {

    List<Stamp> listByOrganization(Integer organizationId);

    List<Stamp> listActiveByOrganization(Integer organizationId);

    Stamp getById(@Param("id") Integer id);
}
