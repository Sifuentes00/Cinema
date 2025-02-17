# Cinema Application

## Описание
Cinema Application — это сложное веб-приложение, разработанное на Spring Boot, которое позволяет управлять данными
о фильмах. Пользователи могут получать информацию о фильмах через REST API, используя как параметры запроса, так и 
параметры пути.

## Требования
- Java 17
- Spring Boot
- Spring MVC
- JUnit 5
- MockMvc
- Maven

## Использование
Получение информации о фильме с помощью параметров запроса.
Вы можете получить информацию о фильме, отправив подобные GET-запросы:
http://localhost:8080/api/query?title=Inception&director=Nolan&releaseYear=2010&genre=Sci-Fi
http://localhost:8080/api/path/Inception/Nolan/2010/Sci-Fi
Пример ответа:
{
"title": "Inception",
"director": "Nolan",
"releaseYear": 2010,
"genre": "Sci-Fi"
}

