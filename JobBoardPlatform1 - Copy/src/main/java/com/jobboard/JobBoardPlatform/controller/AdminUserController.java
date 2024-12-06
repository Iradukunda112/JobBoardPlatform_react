package com.jobboard.JobBoardPlatform.controller;

import com.jobboard.JobBoardPlatform.service.DocumentService;
import com.jobboard.JobBoardPlatform.service.UserService;  // Assuming you have a UserService
import com.jobboard.JobBoardPlatform.model.User;  // Assuming User is your model
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")  // Allow React app to make requests
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final DocumentService documentService;
    private final UserService userService;  // Add UserService for managing users

    @Autowired
    public AdminUserController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }
    @GetMapping("/search")
    public String showSearchForm() {
        return "search-user"; // Thymeleaf template for searching users
    }

    @GetMapping("/search/results")
    @ResponseBody
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String email) {
        try {
            // Ensure you search by username only if it's provided
            List<User> users = userService.searchUsers(username, email);

            if (users.isEmpty()) {
                return ResponseEntity.noContent().build(); // No content found
            }

            return ResponseEntity.ok(users); // Return the list of users
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle server errors
        }
    }




    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user"; // Thymeleaf template for adding user
    }

    // Modify to handle user registration correctly
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user); // Save the user and return the saved entity
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    // ** Edit User Section **
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            // Fetch the user by ID to ensure it exists before updating
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build(); // User not found
            }

            // Update fields selectively (assuming only some fields are editable)
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());

            // Add any other fields you allow updates for

            userService.updateUser(existingUser); // Save the updated user
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);  // Delete the user by ID
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete user: " + e.getMessage());
        }
    }




    // ** File Upload Section **
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            documentService.saveUploadedFile(file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/uploadDocument")
    @ResponseBody
    public List<String> getUploadedFiles() {
        return documentService.listUploadedFiles();  // Return list of uploaded files
    }

    // ** File View Section (inline) **
    @GetMapping("/view/{fileName}")
    public ResponseEntity<?> viewFile(@PathVariable String fileName) {
        Path file = documentService.getFile(fileName);
        if (Files.exists(file)) {
            try {
                String mimeType = documentService.getMimeType(fileName);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, mimeType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(Files.readAllBytes(file));
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error reading file.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ** File Download Section **
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        Path file = documentService.getFile(fileName);
        if (Files.exists(file)) {
            try {
                String mimeType = documentService.getMimeType(fileName);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, mimeType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(Files.readAllBytes(file));
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error reading file.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ** Delete File Section **
    @PostMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        try {
            documentService.deleteFile(fileName);
            return "redirect:/admin/uploadDocument";  // Refresh file list after deletion
        } catch (IOException e) {
            return "redirect:/admin/uploadDocument";  // Handle deletion failure
        }
    }

    // ** Edit User Section **
    @PostMapping("/users/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);  // Assuming UserService handles the update logic
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user: " + e.getMessage());
        }
    }
}
