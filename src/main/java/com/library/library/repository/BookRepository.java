package com.library.library.repository;

import com.library.library.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByBookId(String bookId);
    List<Book> findByBookIdContainingIgnoreCase(String bookId);
    List<Book> findByTitleContainingIgnoreCase(String title);
    boolean existsByBookId(String bookId);
}
