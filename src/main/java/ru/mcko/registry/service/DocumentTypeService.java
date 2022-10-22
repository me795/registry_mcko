package ru.mcko.registry.service;

import ru.mcko.registry.entity.DocumentType;
import ru.mcko.registry.entity.User;

import java.util.List;

public interface DocumentTypeService {

    List<DocumentType> listByName(String searchValue);
    List<DocumentType> listAll();
    List<DocumentType> listByOrganizationDelegate(Integer organizationDelegateId);
}
