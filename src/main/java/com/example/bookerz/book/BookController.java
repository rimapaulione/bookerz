package com.example.bookerz.book;

import com.example.bookerz.rating.JdbcRatingRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RequestMapping("/api/books")
@RestController
public class BookController {

    private final JdbcBookRepository bookRepository;

    public BookController(JdbcBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("")
    List<BookWithRatings> getAllBooks() {
        List<Book> books = bookRepository.findAll();
      return this.getBooksWithAverageRating(books);
    }

    @GetMapping("/{id}")
    Optional<BookWithRatings> getBook(@PathVariable Integer id) {

        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException(id);
        }
        return this.getBookWithAverageRating(book);
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
        return this.getBooksWithAverageRating(books);
    }

    public List<BookWithRatings> getBooksWithAverageRating(List<Book>books) {
        List<BookWithRatings> bookWithRatings = new ArrayList<>();
        for (Book book : books) {
            Optional<Double> averageRating = bookRepository.findAverageRatingByBookId(book.id());
            bookWithRatings.add(new BookWithRatings(book, averageRating));
        }
        return bookWithRatings;
    }

    public Optional<BookWithRatings> getBookWithAverageRating(Optional<Book> book) {

        if (!book.isEmpty()) {
            Book newBook = book.get();
            Optional<Double> averageRating = bookRepository.findAverageRatingByBookId(newBook.id());
            return Optional.of(new BookWithRatings(newBook, averageRating));
        } else {
            return Optional.empty();
        }

    }

}
