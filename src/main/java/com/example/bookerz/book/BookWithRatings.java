package com.example.bookerz.book;

import java.util.Optional;

public class BookWithRatings {
    Integer id;
    String title;
    Integer year;
    Category category;
    String author;
    Optional<Double> averageRating;

public BookWithRatings(Book book, Optional<Double> averageRating) {
        this.id = book.id();
        this.title = book.title();
        this.year = book.year();
        this.category = book.category();
        this.author = book.author();
        this.averageRating = averageRating;
}
    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Integer getYear() {
        return year;
    }
    public Category getCategory() {
        return category;
    }
    public  String getAuthor() {
    return author;
    }

    public Optional<Double> getAverageRating() {
        return averageRating;
    }
}


