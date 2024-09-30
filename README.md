# Bookerz

<p>This application is an educational project built with Spring Boot, utilizing a PostgreSQL database for data storage. Each time the application runs, mock data is generated from data.sql. Users can use Rest API to filter books based on title, year of publication, author, or rating. Additionally, they have the ability to rate books on a scale from 1 to 5 stars, enhancing the overall user experience.
</p>

## RUN
- mvn spring-boot:run
- mvn test

## Used in App:
- Spring Boot;
- Database Postgresql runs with standard Docker;
- Tests run inside Docker Container;

## Main Endpoints
- GET /api/books <br>
- GET /api/books/1 <br>
- GET /api/books/search?rating=1&year=2022&title=spring 

- POST /api/ratings {"book_id":1, "rating":1} <br>
- GET /api/ratings  <br>
- GET /api/ratings/books/1  <br>

## Lessons learned:
- Java is different from Javascript :)
- Creating database tables by hand gives more flexibility but is not easy.
- Need to find out how to use Docker with other projects.



