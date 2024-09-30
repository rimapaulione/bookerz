package com.example.bookerz.book;

import com.example.bookerz.rating.JdbcRatingRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/books")
@RestController
public class BookController {

    private final JdbcBookRepository bookRepository;
    private final BookExistById bookExistById;

    public BookController(JdbcBookRepository bookRepository, BookExistById bookExistById) {
        this.bookRepository = bookRepository;
        this.bookExistById = bookExistById;
    }

    @GetMapping("")
    List<BookWithRatings> getAllBooks() {
        List<Book> books = bookRepository.findAll();
      return bookRepository.getBooksWithAverageRating(books);
    }

    @GetMapping("/{id}")
    List<BookWithRatings> getBook(@PathVariable Integer id) {
        bookExistById.checkExistingBook((id));
        Book book = bookRepository.findById(id).get();
        List<BookWithRatings> bookWithRatings = new ArrayList<>();
        Optional<Double> averageRating = bookRepository.findAverageRatingByBookId(book.id());
        bookWithRatings.add(new BookWithRatings(book, averageRating));
        return bookWithRatings;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Book book) {
         bookRepository.create(book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Book book, @PathVariable Integer id) {
        bookRepository.update(book,id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        bookRepository.delete(id);
    }

    @GetMapping("/search")
    List<BookWithRatings> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer rating) {
       List<Book> books= bookRepository.searchBooks(title, author, year, rating);
        return bookRepository.getBooksWithAverageRating(books);
    }

}
