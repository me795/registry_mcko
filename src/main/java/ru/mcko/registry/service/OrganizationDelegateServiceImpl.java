package ru.mcko.registry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mcko.registry.entity.DocumentType;
import ru.mcko.registry.entity.OrganizationDelegate;
import ru.mcko.registry.repository.read.DocumentTypeRepositoryRead;
import ru.mcko.registry.repository.read.OrganizationDelegateRepositoryRead;
import ru.mcko.registry.repository.write.OrganizationDelegateRepositoryWrite;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrganizationDelegateServiceImpl implements OrganizationDelegateService{

    private final OrganizationDelegateRepositoryWrite organizationDelegateRepositoryWrite;
    private final OrganizationDelegateRepositoryRead organizationDelegateRepositoryRead;

    private final DocumentTypeService documentTypeService;

    public OrganizationDelegateServiceImpl(OrganizationDelegateRepositoryWrite organizationDelegateRepositoryWrite, OrganizationDelegateRepositoryRead organizationDelegateRepositoryRead, DocumentTypeRepositoryRead documentTypeRepositoryRead, DocumentTypeService documentTypeService) {
        this.organizationDelegateRepositoryWrite = organizationDelegateRepositoryWrite;
        this.organizationDelegateRepositoryRead = organizationDelegateRepositoryRead;
        this.documentTypeService = documentTypeService;
    }


    @Override
    public List<OrganizationDelegate> listByOrganization(Integer organizationId) {
        return organizationDelegateRepositoryRead.listByOrganization(organizationId);
    }

    @Override
    public List<OrganizationDelegate> listActiveByOrganization(Integer organizationId) {
        return organizationDelegateRepositoryRead.listActiveByOrganization(organizationId);
    }

    @Override
    public OrganizationDelegate getById(Integer organizationDelegateId) {
        return organizationDelegateRepositoryRead.getById(organizationDelegateId);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public Integer create(OrganizationDelegate organizationDelegate) {
        var organizationDelegateId = organizationDelegateRepositoryWrite.create(organizationDelegate);
        organizationDelegate.setId(organizationDelegateId);
        if (organizationDelegate.getDocumentTypeList() != null) {
            var newDocTypeList = organizationDelegate.getDocumentTypeList();
            for (var docType : newDocTypeList) {
                organizationDelegateRepositoryWrite.addDocumentType(organizationDelegateId, docType.getId());
            }
        }
        return organizationDelegateId;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void update(OrganizationDelegate organizationDelegate) {
        organizationDelegateRepositoryWrite.update(organizationDelegate);
        if (organizationDelegate.getDocumentTypeList() != null) {
            var newDocTypeList = organizationDelegate.getDocumentTypeList();
            var newDocTypeMap = newDocTypeList.stream()
                    .collect(Collectors.toMap(DocumentType::getId, Function.identity()));
            var currentDocTypeList = documentTypeService.listByOrganizationDelegate(organizationDelegate.getId());
            var currentDocTypeMap = currentDocTypeList.stream()
                    .collect(Collectors.toMap(DocumentType::getId, Function.identity()));

            for (Map.Entry<Integer, DocumentType> docType : currentDocTypeMap.entrySet()) {
                if (!newDocTypeMap.containsKey(docType.getKey())) {
                    organizationDelegateRepositoryWrite.deleteDocumentType(organizationDelegate.getId(), docType.getKey());
                }
            }

            for (Map.Entry<Integer, DocumentType> docType : newDocTypeMap.entrySet()) {
                if (!currentDocTypeMap.containsKey(docType.getKey())) {
                    organizationDelegateRepositoryWrite.addDocumentType(organizationDelegate.getId(), docType.getKey());
                }
            }
        }else{
            organizationDelegateRepositoryWrite.deleteAllDocumentType(organizationDelegate.getId());
        }
    }

    @Override
    public void delete(Integer organizationDelegateId) {
        organizationDelegateRepositoryWrite.delete(organizationDelegateId);
    }

    @Override
    public void deleteAllByOrganization(Integer organizationId) {
        organizationDelegateRepositoryWrite.deleteAllByOrganization(organizationId);
    }
}
