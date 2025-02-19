package com.map_study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/files/";

    @Operation(summary = "파일 다운로드", description = "주어진 파일명을 통해 파일을 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/files/{filename}")
    public Resource getFile(@PathVariable String filename) throws Exception {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        return new UrlResource(filePath.toUri());
    }
}
