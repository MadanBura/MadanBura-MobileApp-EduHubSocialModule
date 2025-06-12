package com.connect.eduHub.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
	
	   @Value("${file.upload-dir}")
	    private String uploadDir;

	    @Value("${server.base-url}")
	    private String baseUrl;
	
//    private final String uploadDir = "uploads/";

    public String storeFile(MultipartFile file) throws IOException {
        // Validate file type
    	 String contentType = file.getContentType();
         if (contentType == null || !contentType.startsWith("image/")) {
             throw new IllegalArgumentException("Only image files are allowed");
         }

         Files.createDirectories(Paths.get(uploadDir));

         String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
         Path path = Paths.get(uploadDir).resolve(fileName);
         Files.write(path, file.getBytes());

         return baseUrl + "/uploads/" + fileName; // Only return relative path
//         return fileName;
    }
}
