package ru.mcko.registry.service;

import org.apache.ibatis.annotations.Param;
import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.Organization;

import java.util.List;
import java.util.Map;

public interface OrganizationService {

    DataTablesOutput<Organization> listForDataTable(DataTablesInput dataTablesInput);

    List<Organization> listByName(String searchValue);
    List<Organization> listFull(Map<String,Object> params);

    Long count();

    Long countAll();

    Organization getById(Integer organizationId);

    Integer create(Organization organization);

    void update(Organization organization);

    void delete(Integer organizationId);

}
