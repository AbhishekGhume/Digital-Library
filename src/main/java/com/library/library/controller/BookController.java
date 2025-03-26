package com.library.library.controller;

import com.library.library.model.AvailabilityStatus;
import com.library.library.model.Book;
import com.library.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String index(Model model) {
        List<Book> allBooks = bookService.getAllBooks();
        model.addAttribute("totalBooks", allBooks.size());
        model.addAttribute("availableBooks", allBooks.stream()
                .filter(b -> b.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE)
                .count());
        model.addAttribute("checkedOutBooks", allBooks.stream()
                .filter(b -> b.getAvailabilityStatus() == AvailabilityStatus.CHECKED_OUT)
                .count());
        return "index";
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "all-books";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookService.addBook(book);
            return "redirect:/books/all";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "edit-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable String id, @ModelAttribute Book book,
                             RedirectAttributes redirectAttributes) {
        try {
            bookService.updateBook(id, book);
            return "redirect:/books/all";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/books/all";
    }

    @PostMapping("/search")
    public String searchBooks(@RequestParam String query, Model model) {
        model.addAttribute("books", bookService.searchBooks(query));
        return "search-results";
    }
}