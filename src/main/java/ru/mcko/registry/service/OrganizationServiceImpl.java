package ru.mcko.registry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.*;
import ru.mcko.registry.repository.read.OrganizationRepositoryRead;
import ru.mcko.registry.repository.write.OrganizationRepositoryWrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService
{
    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
    private final OrganizationRepositoryWrite organizationRepositoryWrite;
    private final OrganizationRepositoryRead organizationRepositoryRead;
    private final OrganizationDelegateService organizationDelegateService;
    private final StampService stampService;

    public OrganizationServiceImpl(OrganizationRepositoryWrite organizationRepositoryWrite, OrganizationRepositoryRead organizationRepositoryRead, OrganizationDelegateService organizationDelegateService, StampService stampService) {
        this.organizationRepositoryWrite = organizationRepositoryWrite;
        this.organizationRepositoryRead = organizationRepositoryRead;
        this.organizationDelegateService = organizationDelegateService;
        this.stampService = stampService;
    }

    @Override
    public DataTablesOutput<Organization> listForDataTable(DataTablesInput dataTablesInput) {

        var params = new HashMap<String, Object>();

        params.put("searchValue","%" + dataTablesInput.getSearch().getValue().toLowerCase() + "%");
        params.put("limit",dataTablesInput.getLength());
        params.put("offset",dataTablesInput.getStart());
        var orderColumnNum = dataTablesInput.getOrder().get(0).getColumn();
        var orderColumn = dataTablesInput.getColumns().get(orderColumnNum).getName();
        params.put("orderColumn",orderColumn);
        if (dataTablesInput.getOrder().get(0).getDir().equals("asc")){
            params.put("asc",1);
        }

        var dataTablesOutput = new DataTablesOutput<Organization>();
        dataTablesOutput.setDraw(dataTablesInput.getDraw());
        dataTablesOutput.setRecordsTotal(organizationRepositoryRead.countAll());
        dataTablesOutput.setRecordsFiltered(organizationRepositoryRead.countWithFilter(params));
        dataTablesOutput.setData(organizationRepositoryRead.list(params));

        return dataTablesOutput;
    }

    @Override
    public List<Organization> listByName(String searchValue) {
        return organizationRepositoryRead.listByName("%" + searchValue.toLowerCase() + "%");
    }

    @Override
    public Long count() {
        return organizationRepositoryRead.count();
    }

    @Override
    public Long countAll() {
        return organizationRepositoryRead.countAll();
    }

    @Override
    public List<Organization> listFull(Map<String,Object> params) {
        return organizationRepositoryRead.listFull(params);
    }

    @Override
    public Organization getById(Integer organizationId) {
        return organizationRepositoryRead.getById(organizationId);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public Integer create(Organization organization) {

        UserDetails userDetails = getUserDetails();

        var organizationId = organizationRepositoryWrite.create(organization);

        if (organization.getStampList() != null){
            for (var stamp : organization.getStampList()){
                stamp.setOrganizationId(organizationId);
                var newStampId = stampService.create(stamp);
                logger.info(String.format("stamp with id %s was created by user %s ", newStampId, userDetails.getUsername()));
            }
        }
        if (organization.getOrganizationDelegateList() != null){
            for (var organizationDelegate : organization.getOrganizationDelegateList()){
                organizationDelegate.setOrganizationId(organizationId);
                var newODId = organizationDelegateService.create(organizationDelegate);
                logger.info(String.format("organizationDelegate with id %s was created by user %s ", newODId, userDetails.getUsername()));
            }
        }
        logger.info(String.format("organization with id %s was created by user %s ", organizationId, userDetails.getUsername()));
        return organizationId;
    }

    private UserDetails getUserDetails() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) auth.getPrincipal();
        return userDetails;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void update(Organization organization) {

        UserDetails userDetails = getUserDetails();

        organizationRepositoryWrite.update(organization);
        updateStampList(organization);
        updateOrganizationDelegateList(organization);
        logger.info(String.format("organization with id %s was updated by user %s ", organization.getId(), userDetails.getUsername()));

    }

    private void updateStampList(Organization organization) {

        UserDetails userDetails = getUserDetails();

        if (organization.getStampList() != null) {
            var newStampList = organization.getStampList();

            var currentStampList = stampService.listActiveByOrganization(organization.getId());
            var currentStampMap = currentStampList.stream()
                    .collect(Collectors.toMap(Stamp::getId, Function.identity()));

            for (var stamp : newStampList){
                if (stamp.getId() == null){
                    stamp.setOrganizationId(organization.getId());
                    var newStampId = stampService.create(stamp);
                    logger.info(String.format("stamp with id %s was created by user %s ", newStampId, userDetails.getUsername()));
                }
            }
            var newStampMap = newStampList.stream()
                    .collect(Collectors.toMap(Stamp::getId, Function.identity()));

            for (Map.Entry<Integer, Stamp> stampEntry : currentStampMap.entrySet()) {

                if (!newStampMap.containsKey(stampEntry.getKey())){
                    stampService.delete(stampEntry.getKey());
                    logger.info(String.format("stamp with id %s was deleted by user %s ", stampEntry.getKey(), userDetails.getUsername()));
                }else{
                    var stampForUpdate = newStampMap.get(stampEntry.getKey());
                    stampForUpdate.setOrganizationId(organization.getId());
                    stampService.update(stampForUpdate);
                    logger.info(String.format("stamp with id %s was updated by user %s ", stampEntry.getKey(), userDetails.getUsername()));
                }
            }

        }else{
            stampService.deleteAllByOrganization(organization.getId());
            logger.info(String.format("all stamps from organization %s were deleted by user %s ", organization.getId(), userDetails.getUsername()));
        }
    }

    private void updateOrganizationDelegateList(Organization organization) {

        UserDetails userDetails = getUserDetails();

        if (organization.getOrganizationDelegateList() != null) {
            var newOrgDelegateList = organization.getOrganizationDelegateList();

            var currentOrgDelegateList = organizationDelegateService.listActiveByOrganization(organization.getId());
            var currentOrgDelegateMap = currentOrgDelegateList.stream()
                    .collect(Collectors.toMap(OrganizationDelegate::getId, Function.identity()));

            for (var orgDelegate : newOrgDelegateList){
                if (orgDelegate.getId() == null){
                    orgDelegate.setOrganizationId(organization.getId());
                    var newODId = organizationDelegateService.create(orgDelegate);
                    logger.info(String.format("organizationDelegate with id %s was created by user %s ", newODId, userDetails.getUsername()));
                }
            }
            var newOrgDelegateMap = newOrgDelegateList.stream()
                    .collect(Collectors.toMap(OrganizationDelegate::getId, Function.identity()));

            for (Map.Entry<Integer, OrganizationDelegate> orgDelegateEntry : currentOrgDelegateMap.entrySet()) {
                if (!newOrgDelegateMap.containsKey(orgDelegateEntry.getKey())){
                    organizationDelegateService.delete(orgDelegateEntry.getKey());
                    logger.info(String.format("organizationDelegate with id %s was deleted by user %s ", orgDelegateEntry.getKey(), userDetails.getUsername()));
                }else{
                    var orgDelegateForUpdate = newOrgDelegateMap.get(orgDelegateEntry.getKey());
                    orgDelegateForUpdate.setOrganizationId(organization.getId());
                    organizationDelegateService.update(orgDelegateForUpdate);
                    logger.info(String.format("organizationDelegate with id %s was updated by user %s ", orgDelegateEntry.getKey(), userDetails.getUsername()));
                }
            }

        }else{
            organizationDelegateService.deleteAllByOrganization(organization.getId());
            logger.info(String.format("all organizationDelegates from organization %s were deleted by user %s ", organization.getId(), userDetails.getUsername()));
        }
    }

    @Override
    public void delete(Integer organizationId) {
        organizationRepositoryWrite.delete(organizationId);
    }
}
