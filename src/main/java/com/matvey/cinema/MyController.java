package com.matvey.cinema;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyController {

    // GET с Query Parameters: http://localhost:8080/api/query?name=John&age=30
    @GetMapping("/query")
    public Response getWithQueryParams(@RequestParam String name, @RequestParam int age) {
        return new Response("Received query parameters", name, String.valueOf(age));
    }

    // GET с Path Parameters: http://localhost:8080/api/path/John/30
    @GetMapping("/path/{name}/{age}")
    public Response getWithPathParams(@PathVariable String name, @PathVariable int age) {
        return new Response("Received path parameters", name, String.valueOf(age));
    }
}
