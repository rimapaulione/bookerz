package com.example.bookerz.book;

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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JdbcBookRepository bookRepository;

    private final List<Book> books = new ArrayList<>();

    @BeforeEach
    void setUp() {

        Book book = new Book(1,
                "Testing book",
                2022,
                Category.FANTASY,
                "Author");
        books.add(book);
    }

    @Test
    void shouldFindAllBooks() throws Exception {

        when(bookRepository.findAll()).thenReturn(books);
        mvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(books.size())));
    }

    @Test
    void shouldFindOneBook() throws Exception {

        Book book = books.get(0);
        when(bookRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(book));
        mvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(book.id())))
                .andExpect(jsonPath("$.author", is(book.author())))
                .andExpect(jsonPath("$.year", is(book.year())))
                .andExpect(jsonPath("$.category", is(book.category().name())));

    }

    @Test
    void shouldReturnNotFoundWithInvalid() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/books/99"))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldCreate() throws Exception {

        Book newBook = new Book(1,
                "Monday Morning Run",
                2022,
                Category.FANTASY,
                "Puchini");

        mvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook))
        ).andExpect(status().isCreated());

    }

    @Test
    void shouldUpdate() throws Exception {

        Book update = new Book(1,
                "Monday Morning Run1",
                2022,
                Category.FANTASY,
                "Puchini");

        mvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        ).andExpect(status().isNoContent());
    }

    @Test
    void shouldDelete() throws Exception {


        mvc.perform(MockMvcRequestBuilders.delete("/api/books/1")
        ).andExpect(status().isNoContent());

    }

}