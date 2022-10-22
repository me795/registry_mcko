package ru.mcko.registry.controller;

import org.springframework.web.bind.annotation.*;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.entity.DocumentType;
import ru.mcko.registry.service.DocumentTypeService;

import java.util.List;

@RestController
public class DocumentTypeRestController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeRestController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }


    @PostMapping("/documents/search")
    public List<DocumentType> searchDocumentTypes(@RequestParam("searchValue") String searchValue) {

        return documentTypeService.listByName(searchValue);
    }


}
