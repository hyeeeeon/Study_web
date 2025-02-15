package com.map_study.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/files/";

    @GetMapping("/files/{filename}")
    public Resource getFile(@PathVariable String filename) throws Exception {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        return new UrlResource(filePath.toUri());
    }
}