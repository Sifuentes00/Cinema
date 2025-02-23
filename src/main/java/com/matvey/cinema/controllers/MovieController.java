package com.matvey.cinema.controllers;

import com.matvey.cinema.model.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//do
@RestController
@RequestMapping("/api")
public class MovieController {

    //http://localhost:8080/api/query?id=1&title=Inception&director=Nolan&releaseYear=2010&genre=Sci-Fi
    @GetMapping("/query")
    public Movie getWithQueryParams(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String director,
            @RequestParam int releaseYear,
            @RequestParam String genre) {
        return new Movie(id, title, director, releaseYear, genre);
    }

    //http://localhost:8080/api/path/1/Inception/Nolan/2010/Sci-Fi
    @GetMapping("/path/{id}/{title}/{director}/{releaseYear}/{genre}")
    public Movie getWithPathParams(
            @PathVariable Long id,
            @PathVariable String title,
            @PathVariable String director,
            @PathVariable int releaseYear,
            @PathVariable String genre) {
        return new Movie(id, title, director, releaseYear, genre);
    }
}
