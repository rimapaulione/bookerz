package com.example.bookerz.rating;

import com.example.bookerz.book.Book;
import com.example.bookerz.book.BookNotFoundException;
import com.example.bookerz.book.JdbcBookRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/ratings")
@RestController
public class RatingController {

    private final JdbcRatingRepository ratingRepository;
    private final JdbcBookRepository bookRepository;

    public RatingController(JdbcRatingRepository ratingRepository, JdbcBookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository =  bookRepository;
    }

    @GetMapping("")
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @GetMapping("/book/{book_id}")
    public List<Rating> getRatingsByBookId(@PathVariable Integer book_id) {
        return ratingRepository.findByBookId(book_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Rating rating) {
        checkExistingBook(rating.book_id());
        ratingRepository.create(rating);
    }

    void checkExistingBook(Integer id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException(id);
        }
    }

}
