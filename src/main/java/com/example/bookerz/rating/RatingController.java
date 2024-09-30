package com.example.bookerz.rating;

import com.example.bookerz.book.BookExistById;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/ratings")
@RestController
public class RatingController {

    private final JdbcRatingRepository ratingRepository;
    private final  BookExistById bookExistById;

    public RatingController(JdbcRatingRepository ratingRepository, BookExistById bookExistById) {
        this.ratingRepository = ratingRepository;
        this.bookExistById = bookExistById;
    }

    @GetMapping("")
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @GetMapping("/book/{book_id}")
    public List<Rating> getRatingsByBookId(@PathVariable Integer book_id) {
       bookExistById.checkExistingBook(book_id);
        return ratingRepository.findByBookId(book_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Rating rating) {
        bookExistById.checkExistingBook(rating.book_id());
       ratingRepository.create(rating);
    }
}
