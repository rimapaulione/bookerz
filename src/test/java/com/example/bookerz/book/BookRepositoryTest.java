package com.example.bookerz.book;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Import(JdbcBookRepository.class)
class BookRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("booker")
                    .withUsername("myuser")
                    .withPassword("secret");

    static {
        postgresContainer.start();
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
    private JdbcBookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        bookRepository.create(new Book(1,
                "Testing book",
                2022,
                Category.FANTASY,
                "Author"));
    }

    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
    }

    @Test
    void findById() {
        List<Book> books = bookRepository.findAll();
        Integer id = books.getFirst().id();
        Book book = bookRepository.findById(id).get();
        assertEquals(book.title(), "Testing book");
    }

    @Test
    void create() {
        bookRepository.deleteAll();
        assertEquals(0, bookRepository.findAll().size());
        bookRepository.create(new Book(1,
                "Testing book",
                2022,
                Category.FANTASY,
                "Author"));
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    void update() {
        List<Book> books = bookRepository.findAll();
        Integer id = books.getFirst().id();

        Book updatedBook = new Book(
                id,
                "Updated Title",
                2024,
                Category.SCIENCE,
                "Updated Author"
        );
        bookRepository.update( updatedBook, id);
        Optional<Book> updateBookFromDb = bookRepository.findById(id);
        assertEquals(updatedBook.title(), "Updated Title");
        assertEquals(2024, updateBookFromDb.get().year());
        assertEquals(updatedBook.category(), Category.SCIENCE);
    }

    @Test
    void delete() {
        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
        Integer id = books.getFirst().id();
        bookRepository.delete(id);
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test
    void searchBooksByTitle() {
        List<Book> books = bookRepository.searchBooks("Testing book",null, null, null);
        assertEquals(1, books.size());
        assertEquals("Testing book", books.getFirst().title());
    }

    @Test
    void searchBooksByAuthor() {
        List<Book> books = bookRepository.searchBooks(null,"Author", null, null);
        assertEquals(1, books.size());
        assertEquals("Author", books.getFirst().author());
    }
    @Test
    void searchBooksByYear() {
        List<Book> books = bookRepository.searchBooks(null,null, 2022, null);
        assertEquals(1, books.size());
        assertEquals(2022, books.getFirst().year());
    }


    //Neveikia,nes reikia prideti reitinga
//    @Test
//    void searchBooksByRating() {
//
//
//    }

    @Test
    void searchBooksNotFound() {
        List<Book> books = bookRepository.searchBooks("X", "X", 2024, 5);
        assertEquals(0, books.size());
    }

    @Test
    void deleteAll() {
        bookRepository.deleteAll();
        assertEquals(0, bookRepository.findAll().size());
    }
}