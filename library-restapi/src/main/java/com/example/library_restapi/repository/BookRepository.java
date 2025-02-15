package com.example.library_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.library_restapi.model.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBorrowerId(Long borrowerId);
}
