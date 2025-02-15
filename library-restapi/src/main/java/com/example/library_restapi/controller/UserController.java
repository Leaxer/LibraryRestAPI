package com.example.library_restapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library_restapi.model.User;
import com.example.library_restapi.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Map<String, Object>> getAllUsers() {
        return userService.getAllUsersWithBooks();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{userId}/borrow/{bookId}")
    public String borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return userService.borrowBook(userId, bookId);
    }

    @PostMapping("/{userId}/return/{bookId}")
    public String returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return userService.returnBook(userId, bookId);
    }

    @GetMapping("/{id}/borrowed-books")
    public List<String> getBorrowedBooksByUser(@PathVariable Long id) {
        return userService.getBorrowedBooksWithDeadline(id);
    }
}
