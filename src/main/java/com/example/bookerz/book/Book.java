package com.example.bookerz.book;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;

public record Book (

        Integer id,
        @NotEmpty String title,
        @Positive Integer year,
        Category category,
        @NotEmpty String author

){
}
