package com.example.bookerz.rating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
@Import(JdbcRatingRepository.class)
class RatingRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("booker")
                    .withUsername("myuser")
                    .withPassword("secret");

    static {
        postgresContainer.start(); // Start the container before any tests run
    }

    @Autowired
    private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.initialization-mode", () -> "always");
    }

    @Autowired
    private JdbcRatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        ratingRepository.create(new Rating(1,5,5));
    }

    @Test
    void findAll() {
        List<Rating> ratings = ratingRepository.findAll();
        assertEquals(1, ratings.size());
    }

    @Test
    void findByBookId() {
        List<Rating> ratings = ratingRepository.findAll();
        Integer book_id = ratings.getFirst().book_id();
        List<Rating> ratingsWithBookId= ratingRepository.findByBookId(book_id);
        assertEquals(1, ratingsWithBookId.size());
        assertEquals(5, ratingsWithBookId.getFirst().rating());
        assertEquals(5, ratingsWithBookId.getFirst().book_id());
    }

    @Test
    void findByBookIdNoRatings() {
        Integer book_id = 99;
        List<Rating> ratingsWithBookId= ratingRepository.findByBookId(book_id);
        assertEquals(0, ratingsWithBookId.size());
    }

    @Test
    void create() {
        ratingRepository.deleteAll();
        assertEquals(0, ratingRepository.findAll().size());
        ratingRepository.create(new Rating(1,1,1));
        assertEquals(1, ratingRepository.findAll().size());
    }

    @Test
    void deleteAll() {
        ratingRepository.deleteAll();
        assertEquals(0, ratingRepository.findAll().size());
    }
}