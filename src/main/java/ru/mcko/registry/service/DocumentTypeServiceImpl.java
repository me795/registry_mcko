package ru.mcko.registry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mcko.registry.entity.DocumentType;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.repository.read.DocumentTypeRepositoryRead;

import java.util.List;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepositoryRead documentTypeRepositoryRead;

    public DocumentTypeServiceImpl(DocumentTypeRepositoryRead documentTypeRepositoryRead) {
        this.documentTypeRepositoryRead = documentTypeRepositoryRead;
    }


    @Override
    public List<DocumentType> listByName(String searchValue) {
        return documentTypeRepositoryRead.listByName("%"+searchValue.toLowerCase()+"%");
    }

    @Override
    public List<DocumentType> listAll() {
        return documentTypeRepositoryRead.listAll();
    }

    @Override
    public List<DocumentType> listByOrganizationDelegate(Integer organizationDelegateId) {
        return documentTypeRepositoryRead.listByOrganizationDelegate(organizationDelegateId);
    }
}
