package com.matvey.cinema;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class MyController {

    //http://localhost:8080/api/query?title=Inception&director=Nolan&releaseYear=2010&genre=Sci-Fi
    @GetMapping("/query")
    public Movie getWithQueryParams(
            @RequestParam String title,
            @RequestParam String director,
            @RequestParam int releaseYear,
            @RequestParam String genre) {
        return new Movie(title, director, releaseYear, genre);
    }

    //http://localhost:8080/api/path/Inception/Nolan/2010/Sci-Fi
    @GetMapping("/path/{title}/{director}/{releaseYear}/{genre}")
    public Movie getWithPathParams(
            @PathVariable String title,
            @PathVariable String director,
            @PathVariable int releaseYear,
            @PathVariable String genre) {
        return new Movie(title, director, releaseYear, genre);
    }
}
