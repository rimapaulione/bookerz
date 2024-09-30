package com.example.bookerz.book;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BookExistById {

    private final JdbcBookRepository bookRepository;

    public BookExistById(JdbcBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void checkExistingBook(Integer bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
    }
}
