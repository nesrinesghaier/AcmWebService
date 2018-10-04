package com.eniso.acmwebservice.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private Path rootLocation = Paths.get("upload-dir");
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            logger.error("Error in copying file!");
            throw new RuntimeException("FAIL!");
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                logger.error("Error in loading file!");
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            logger.error("Error in loading file!");
            throw new RuntimeException("FAIL!");
        }
    }


    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            logger.error("Could not initialize storage!");
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}