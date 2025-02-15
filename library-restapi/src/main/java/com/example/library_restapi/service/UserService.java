package com.example.library_restapi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.repository.UserRepository;
import com.example.library_restapi.model.Book;
import com.example.library_restapi.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Map<String, Object>> getAllUsersWithBooks() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());

            // Kullanıcının ödünç aldığı kitapları alıyoruz
            List<Book> borrowedBooks = bookRepository.findByBorrowerId(user.getId());
            List<Map<String, Object>> booksList = borrowedBooks.stream().map(book -> {
                Map<String, Object> bookData = new HashMap<>();
                bookData.put("id", book.getId());
                bookData.put("title", book.getTitle());
                bookData.put("author", book.getAuthor());
                bookData.put("borrowDate", book.getBorrowDate());
                bookData.put("dueDate", book.getBorrowDate().plusDays(14));
                return bookData;
            }).collect(Collectors.toList());

            userData.put("borrowedBooks", booksList);
            return userData;
        }).collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String borrowBook(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            User user = userOpt.get();
            Book book = bookOpt.get();

            if (!book.isBorrowed()) {
                book.borrowBook(user.getId()); // Artık kitap ve kullanıcı güncelleniyor
                bookRepository.save(book);
                userRepository.save(user); // Kullanıcı da güncellenmeli!
                return "Kitap ödünç alındı: " + book.getTitle();
            } else {
                return "Kitap zaten ödünç alınmış.";
            }
        }
        return "Kullanıcı veya kitap bulunamadı.";
    }

    public String returnBook(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            User user = userOpt.get();
            Book book = bookOpt.get();

            if (book.isBorrowed() && book.getBorrowerId().equals(user.getId())) {
                book.returnBook(); // Kitap ve kullanıcı güncellendi
                bookRepository.save(book);
                userRepository.save(user); // Kullanıcı değiştiği için kaydetmeliyiz!
                return "Kitap iade edildi: " + book.getTitle();
            } else {
                return "Kitap bu kullanıcıda değil.";
            }
        }
        return "Kullanıcı veya kitap bulunamadı.";
    }

    public List<String> getBorrowedBooksWithDeadline(Long userId) {
        List<Book> borrowedBooks = bookRepository.findByBorrowerId(userId);

        if (!borrowedBooks.isEmpty()) {
            return borrowedBooks.stream()
                    .map(book -> {
                        LocalDate deadline = book.getBorrowDate().plusDays(14);
                        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
                        return "Kitap: " + book.getTitle() +
                                " | Yazar: " + book.getAuthor() +
                                " | İade Tarihi: " + deadline +
                                " | Kalan Gün: " + remainingDays;
                    })
                    .collect(Collectors.toList());
        }
        return List.of("Kullanıcıya ait ödünç kitap bulunamadı.");
    }

}
