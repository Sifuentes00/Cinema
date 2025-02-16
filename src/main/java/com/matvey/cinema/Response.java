package com.matvey.cinema;

public class Response {
    private String message;
    private String name;
    private String age;

    // Конструктор
    public Response(String message, String name, String age) {
        this.message = message;
        this.name = name;
        this.age = age;
    }

    // Геттеры
    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
