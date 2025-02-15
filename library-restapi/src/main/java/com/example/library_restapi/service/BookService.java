package com.example.library_restapi.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.model.Book;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public String deleteBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            if (book.isBorrowed()) {
                return "This book cannot be deleted because it is currently on borrow.";
            }

            bookRepository.deleteById(id);
            return "Book has been succesfully deleted.";
        }

        return "Book couldn't find.";
    }

    public List<Book> getBooksByUserId(Long userId) {
        return bookRepository.findByBorrowerId(userId);
    }
}
