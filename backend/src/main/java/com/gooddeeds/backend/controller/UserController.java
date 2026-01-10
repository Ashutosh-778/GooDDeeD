package com.gooddeeds.backend.controller;
//
//import com.gooddeeds.backend.model.User;
//import com.gooddeeds.backend.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    // ✅ CREATE USER
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
//
//        User user = userService.createUser(
//                request.getName(),
//                request.getEmail(),
//                request.getPassword()
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(user);
//    }
//
//    // ✅ GET USER BY ID
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
//
//        User user = userService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }
//
//    // ✅ GET USER BY EMAIL
//    @GetMapping("/by-email")
//    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
//
//        User user = userService.getUserByEmail(email);
//        return ResponseEntity.ok(user);
//    }
//}






import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/by-email")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}



