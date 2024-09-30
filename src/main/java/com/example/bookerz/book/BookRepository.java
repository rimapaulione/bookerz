package com.example.bookerz.book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Integer id);
    void create(Book book);
    void update(Book book, Integer id);
    void delete(Integer id);
    List<Book> searchBooks(String title, String author, Integer year, Integer rating);


}
