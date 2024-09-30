package com.example.bookerz.rating;


import com.example.bookerz.book.JdbcBookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)

class RatingControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JdbcRatingRepository ratingRepository;

    @MockBean
    JdbcBookRepository bookRepository;

    private final List<Rating> ratings = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Rating rating = new Rating(
              1,
              5,
                1
        );
        ratings.add(rating);
    }

    @Test
    void shouldReturnAllRatings() throws Exception {
        when(ratingRepository.findAll()).thenReturn(ratings);
        mvc.perform(MockMvcRequestBuilders.get( "/api/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(ratings.size())));
    }

    @Test
    void shouldReturnRatingByBookId() throws Exception {

        Rating rating = ratings.get(0);

        when(ratingRepository.findByBookId(ArgumentMatchers.any())).thenReturn(ratings);
        mvc.perform(MockMvcRequestBuilders.get("/api/ratings/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(rating.id())))
                .andExpect(jsonPath("$[0].rating", is(rating.rating())))
                .andExpect(jsonPath("$[0].book_id", is(rating.book_id())));
    }
}