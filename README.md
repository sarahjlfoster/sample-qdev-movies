# Movie Service - Spring Boot Demo Application 🏴‍☠️

Ahoy matey! Welcome to our treasure chest of movies - a swashbuckling movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a pirate twist!

## Features

- **Movie Treasure Chest**: Browse 12 classic movie treasures with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🔍 Treasure Hunt (Search & Filter)**: NEW! Search for movie treasures by name, ID, or genre with our pirate-themed interface
- **REST API for Treasure Hunting**: JSON API endpoints for developers to search movies programmatically
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices, from ship to shore!
- **Modern Pirate UI**: Dark theme with gradient backgrounds, smooth animations, and pirate flair

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating
- **Pirate Language Support** 🏴‍☠️

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie Treasure Chest**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **Search Treasures**: http://localhost:8080/movies?name=pirate&genre=adventure
- **REST API Search**: http://localhost:8080/movies/search?name=adventure&genre=action

## 🔍 New Search & Filter Features

### Web Interface Search

Navigate to `/movies` and use the treasure hunt form to search by:
- **Movie Name**: Partial text search (case-insensitive)
- **Movie ID**: Exact ID match
- **Genre**: Partial genre search (case-insensitive)

**Example searches:**
- Find movies with "The" in the name: `/movies?name=The`
- Find action movies: `/movies?genre=Action`
- Find specific movie by ID: `/movies?id=5`
- Combine criteria: `/movies?name=Hero&genre=Action`

### REST API Endpoints

#### Search Movies (JSON Response)
```
GET /movies/search
```

**Query Parameters:**
- `name` (optional): Movie name to search for (partial match, case-insensitive)
- `id` (optional): Specific movie ID to find
- `genre` (optional): Genre to filter by (partial match, case-insensitive)

**Note**: At least one parameter is required, ye scallywag!

**Example Requests:**
```bash
# Search by name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=drama"

# Search by ID
curl "http://localhost:8080/movies/search?id=3"

# Multiple criteria
curl "http://localhost:8080/movies/search?name=the&genre=action"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Ahoy! Successfully found 2 movie treasures matching yer search, captain!",
  "treasures": [
    {
      "id": 3,
      "movieName": "The Masked Hero",
      "director": "Chris Moviemaker",
      "year": 2008,
      "genre": "Action/Crime",
      "description": "When a menacing villain wreaks havoc...",
      "duration": 152,
      "imdbRating": 5.0,
      "icon": "🦸"
    }
  ],
  "totalFound": 1,
  "searchParameters": {
    "name": "hero",
    "id": null,
    "genre": "action"
  }
}
```

**Error Response (No Parameters):**
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.",
  "treasures": []
}
```

**No Results Response:**
```json
{
  "success": true,
  "message": "Shiver me timbers! No movie treasures found matching yer search criteria. Try different search terms, ye landlubber!",
  "treasures": [],
  "totalFound": 0
}
```

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Service with treasure hunting methods
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review service
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie treasure data
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       ├── static/css/
│       │   └── movies.css                    # Pirate-themed styling
│       └── templates/
│           ├── movies.html                   # Main treasure chest page with search
│           └── movie-details.html            # Individual movie details
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            └── MoviesControllerTest.java     # Controller tests with search
```

## API Endpoints

### Get All Movies (with optional search)
```
GET /movies
```
Returns an HTML page displaying movies. Supports search parameters:
- `name`: Filter by movie name
- `id`: Filter by movie ID  
- `genre`: Filter by genre

**Examples:**
```
http://localhost:8080/movies                    # All movies
http://localhost:8080/movies?name=adventure     # Movies with "adventure" in name
http://localhost:8080/movies?genre=drama        # Drama movies
http://localhost:8080/movies?id=5               # Movie with ID 5
```

### Search Movies (JSON API)
```
GET /movies/search
```
Returns JSON response with search results. Requires at least one parameter:
- `name`: Movie name search (partial, case-insensitive)
- `id`: Exact movie ID
- `genre`: Genre search (partial, case-insensitive)

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest

# Run with coverage
mvn test jacoco:report
```

The test suite includes:
- **MovieServiceTest**: Tests for treasure hunting (search) functionality
- **MoviesControllerTest**: Tests for web and API endpoints
- Edge cases: empty results, invalid parameters, case sensitivity
- Pirate-themed test messages and assertions

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that at least one search parameter is provided
2. Verify parameter names are correct: `name`, `id`, `genre`
3. Check application logs for detailed error messages

## Contributing

Ahoy! This project welcomes contributions from fellow pirates and landlubbers alike:
- Add more movie treasures to the catalog
- Enhance the pirate-themed UI/UX
- Add new search features (director search, year range, rating filter)
- Improve the responsive design for mobile treasure hunting
- Add more pirate language and personality

## Pirate Language Features 🏴‍☠️

This application includes fun pirate-themed language throughout:
- Search functionality called "treasure hunting"
- Movies referred to as "treasures"
- Pirate-themed error messages and success notifications
- Nautical terminology in code comments and documentation
- Swashbuckling user interface elements

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*"Arrr! May yer movie treasure hunting be fruitful and yer code be bug-free, matey!"* 🏴‍☠️
