package com.connect.eduHub.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.connect.eduHub.dto.UserDTO;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.UserRepo;
import com.connect.eduHub.services.FileStorageService;
import com.connect.eduHub.services.UserService;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final String UPLOAD_DIR = "uploads/profile_images/";

    @Autowired
    private UserRepo userRepository;
    @Autowired private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long userId,
                                                     @RequestParam("file") MultipartFile file) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            String fullImageUrl = fileStorageService.storeFile(file); // Reuse the service
            User user = userOpt.get();
            user.setProfileImage(fullImageUrl);
            System.out.println(fullImageUrl);
            userRepository.save(user);

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
    

    // Optional: Serve image by filename
    @GetMapping("/profile-image/{filename}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + filename);
        if (!Files.exists(path)) return ResponseEntity.notFound().build();

        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
    
    
    @GetMapping("/{userId}/friends")
    public Set<Long> getFriendsOfUser(@PathVariable Long userId) {
        // Call the service method to get the list of friends for the given user
        return userService.getFriendsOfUser(userId);
    }
    
    
    @GetMapping("/getAlluser")
    public List<UserDTO> getAllUsersExcludeSelf(@RequestParam("userId") Long userId) {
        return userService.getAllUsersExcludingSelf(userId); // Replace with actual user fetching logic
    }
}
