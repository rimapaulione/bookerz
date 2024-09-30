package com.example.bookerz.rating;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

@Repository
public class JdbcRatingRepository implements RatingRepository {
private final JdbcClient jdbcClient;

    public JdbcRatingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Rating> findAll() {
        return jdbcClient.sql("SELECT * from ratings")
            .query(Rating.class)
            .list();
    }

    public List<Rating> findByBookId(Integer id) {
        return jdbcClient.sql("SELECT * FROM ratings WHERE book_id = :id")
                .param("id", id)
                .query(Rating.class)
                .list();
    }

    public void create(Rating rating) {
        var updated = jdbcClient.sql("INSERT INTO ratings(rating,book_id) values(?,?)")
                .params(List.of(rating.rating(), rating.book_id()))
                .update();
        Assert.state(updated == 1, "Failed to create rating " + rating.id());
    }

    public void deleteAll() {
        var updated = jdbcClient.sql("delete from ratings")
                .update();
        Assert.state(updated >= 0, "Failed to delete ratings ");
    }
}
