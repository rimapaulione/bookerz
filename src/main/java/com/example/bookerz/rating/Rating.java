package com.example.bookerz.rating;

import jakarta.validation.constraints.*;

public record Rating (
        Integer id,
        @Min(1) @Max(5) Integer rating,
        Integer book_id
){}


