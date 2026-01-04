package com.tms.finalproject_autoshop.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final Path ROOT_FILE_PATH = Paths.get("data");
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<HttpStatusCode> upload(@RequestParam("filename") MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Files.copy(multipartFile.getInputStream(), ROOT_FILE_PATH.resolve(multipartFile.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Path pathToFile = ROOT_FILE_PATH.resolve(filename);

        try {
            Resource resource = new UrlResource(pathToFile.toUri());
            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Hidden
    @GetMapping
    public ResponseEntity<List<String>> getAllFileNames() {
        try {
            List<String> fileNames = Files.walk(ROOT_FILE_PATH, 1)
                    .filter(path -> !path.equals(ROOT_FILE_PATH))
                    .map(Path::toString)
                    .map(path -> path.replaceAll("data\\\\", ""))
                    .toList();
            return new ResponseEntity<>(fileNames, HttpStatus.OK);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "remove-endpoints")
    @DeleteMapping("/{filename}")
    public ResponseEntity<String> delete(@PathVariable String filename) {
        Path pathToFile = ROOT_FILE_PATH.resolve(filename);

        File file = new File(pathToFile.toString());
        if (file.exists()) {
            if (file.delete()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
