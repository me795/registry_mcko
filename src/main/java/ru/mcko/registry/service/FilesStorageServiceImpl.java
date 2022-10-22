package ru.mcko.registry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mcko.registry.entity.FileInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FilesStorageServiceImpl.class);
    private final Path root = Paths.get("");

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public FileInfo save(MultipartFile file) {
        var fileInfo = new FileInfo();
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        var newFileName = uuidAsString + file.getOriginalFilename();
        fileInfo.setName(uploadPath + newFileName);
        fileInfo.setUrl("/img/" + newFileName);
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileInfo.getName()));
            return fileInfo;
        } catch (Exception e) {
            logger.error(String.format("Could not store the file %s", fileInfo.getName()));
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}