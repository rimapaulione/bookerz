package com.example.bookerz.book;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class JdbcBookRepository implements BookRepository {

    private final JdbcClient jdbcClient;

    public JdbcBookRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Book> findAll() {
        return jdbcClient.sql("select * from books")
                .query((rs, rowNum) -> new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        Category.valueOf(rs.getString("category")),
                        rs.getString("author")
                ))
                .list();
    }

    public Optional<Book> findById(Integer id) {
        return jdbcClient.sql("SELECT id,title,year,author,category FROM books WHERE id = :id" )
                .param("id", id)
                .query(Book.class)
                .optional();
    }

    public void create(Book book) {
        var updated = jdbcClient.sql("INSERT INTO books(title,year,category,author) values(?,?,?,?)")
                .params(List.of(book.title(),book.year(),book.category().name(),book.author()))
                .update();

        Assert.state(updated == 1, "Failed to create run " + book.title());
    }

    public void update(Book book, Integer id) {
        var updated = jdbcClient.sql("UPDATE books set title = ?, year = ?, category = ?, author = ? where id = ?")
                .params(List.of(book.title(),book.year(), book.category().name(),book.author(), id))
                .update();

        Assert.state(updated == 1, "Failed to update book " + book.title());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from books where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete book " + id);
    }

    public List<Book> searchBooks(String title, String author, Integer year, Integer rating) {
        return jdbcClient.sql("SELECT b.*, COALESCE(AVG(r.rating), 0) AS averageRating FROM books b " +
                        "LEFT JOIN ratings r ON r.book_id = b.id" +
                        " WHERE (COALESCE(:title::text, '') = '' OR LOWER(title) LIKE LOWER(CONCAT('%', :title::text, '%'))) "+
                        " AND (COALESCE(:author::text, '') = '' OR LOWER(author) LIKE LOWER(CONCAT('%', :author::text, '%')))" +
                        " AND (:year::integer IS NULL OR year = :year::integer)" +
                        " AND (:rating::integer IS NULL OR r.rating = :rating::integer) "+
                        "group by b.id"
                )
                .param("title", title)
                .param("author", author)
                .param("year", year)
                .param("rating", rating)
                .query(Book.class)
                .list();
    }

    public void deleteAll() {
        var updated = jdbcClient.sql("delete from books")
                .update();
        Assert.state(updated >= 0, "Failed to delete books ");
    }

    public Optional<Double> findAverageRatingByBookId(Integer id) {
        return jdbcClient.sql("SELECT COALESCE(AVG(r.rating), 0) AS averageRating FROM books b " +
                        "LEFT JOIN ratings r ON r.book_id = b.id" +
                        " WHERE b.id = :id " +
                        "GROUP BY b.id"
                )
                .param("id", id)
                .query(Double.class)
                .optional();
    }

}