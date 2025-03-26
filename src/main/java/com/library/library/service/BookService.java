package com.library.library.service;


import com.library.library.exception.BookNotFoundException;
import com.library.library.model.AvailabilityStatus;
import com.library.library.model.Book;
import com.library.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Book addBook(Book book) {
        if (bookRepository.existsByBookId(book.getBookId())) {
            throw new IllegalArgumentException("Book ID must be unique");
        }
        validateBook(book);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    public List<Book> searchBooks(String query) {
        List<Book> byAuthor = bookRepository.findByBookIdContainingIgnoreCase(query);
        List<Book> byTitle = bookRepository.findByTitleContainingIgnoreCase(query);

        byTitle.addAll(byAuthor);
        return byTitle.stream().distinct().toList();
    }

    public Book updateBook(String id, Book bookDetails) {
        Book book = getBookById(id);
        if (!book.getBookId().equals(bookDetails.getBookId()) &&
                bookRepository.existsByBookId(bookDetails.getBookId())) {
            throw new IllegalArgumentException("Book ID already exists");
        }
        validateBook(bookDetails);
        book.setBookId(bookDetails.getBookId());
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setGenre(bookDetails.getGenre());
        book.setAvailabilityStatus(bookDetails.getAvailabilityStatus());
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (book.getAvailabilityStatus() == null) {
            throw new IllegalArgumentException("Invalid availability status");
        }
    }
}
