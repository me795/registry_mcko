package ru.mcko.registry.repository.read;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.DocumentType;

import java.util.List;

@Repository
public interface DocumentTypeRepositoryRead {

    List<DocumentType> listByName(String searchValue);

    List<DocumentType> listAll();
    List<DocumentType> listByOrganizationDelegate(Integer organizationDelegateId);
}
