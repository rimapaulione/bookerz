package com.example.bookerz.rating;

import java.util.List;

public interface RatingRepository {
    public List<Rating> findAll();
    public List<Rating> findByBookId(Integer id);
    public void create(Rating rating);
}
