# Movie Search API Documentation 🏴‍☠️

Ahoy, fellow developers! This document provides comprehensive technical documentation for the Movie Search and Filtering API with our swashbuckling pirate theme.

## Overview

The Movie Search API allows ye to hunt for movie treasures using various search criteria. The API supports both HTML responses (for web browsers) and JSON responses (for programmatic access).

## Base URL

```
http://localhost:8080
```

## Authentication

No authentication required, matey! This be a free treasure hunt for all.

## Endpoints

### 1. HTML Movie Search (Web Interface)

**Endpoint:** `GET /movies`

**Description:** Returns an HTML page with movie results. Supports optional search parameters for filtering the treasure chest.

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No | Movie name to search for (partial match, case-insensitive) |
| `id` | Long | No | Specific movie ID to find (exact match) |
| `genre` | String | No | Genre to filter by (partial match, case-insensitive) |

**Response:** HTML page with search form and movie results

**Examples:**
```bash
# Get all movies
GET /movies

# Search by name
GET /movies?name=adventure

# Search by ID
GET /movies?id=5

# Search by genre
GET /movies?genre=drama

# Multiple criteria
GET /movies?name=the&genre=action
```

**Features:**
- Interactive search form with pirate-themed UI
- Real-time search parameter preservation
- Responsive design for mobile treasure hunting
- Pirate-themed success/error messages
- Genre dropdown with available options

---

### 2. JSON Movie Search (REST API)

**Endpoint:** `GET /movies/search`

**Description:** Returns JSON response with search results. Perfect for API consumers and AJAX calls.

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No* | Movie name to search for (partial match, case-insensitive) |
| `id` | Long | No* | Specific movie ID to find (exact match) |
| `genre` | String | No* | Genre to filter by (partial match, case-insensitive) |

*At least one parameter is required, ye scallywag!

**Response Format:**
```json
{
  "success": boolean,
  "message": "string (pirate-themed)",
  "treasures": [Movie],
  "totalFound": number,
  "searchParameters": {
    "name": "string|null",
    "id": "number|null", 
    "genre": "string|null"
  }
}
```

**Movie Object Structure:**
```json
{
  "id": 1,
  "movieName": "The Prison Escape",
  "director": "John Director",
  "year": 1994,
  "genre": "Drama",
  "description": "Two imprisoned men bond over...",
  "duration": 142,
  "imdbRating": 5.0,
  "icon": "🎬"
}
```

**HTTP Status Codes:**
- `200 OK`: Successful search (even if no results found)
- `400 Bad Request`: No search parameters provided
- `500 Internal Server Error`: Server error during search

**Examples:**

#### Successful Search
```bash
curl "http://localhost:8080/movies/search?name=prison"
```

**Response:**
```json
{
  "success": true,
  "message": "Ahoy! Successfully found 1 movie treasure matching yer search, captain!",
  "treasures": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    }
  ],
  "totalFound": 1,
  "searchParameters": {
    "name": "prison",
    "id": null,
    "genre": null
  }
}
```

#### No Results Found
```bash
curl "http://localhost:8080/movies/search?name=nonexistent"
```

**Response:**
```json
{
  "success": true,
  "message": "Shiver me timbers! No movie treasures found matching yer search criteria. Try different search terms, ye landlubber!",
  "treasures": [],
  "totalFound": 0,
  "searchParameters": {
    "name": "nonexistent",
    "id": null,
    "genre": null
  }
}
```

#### Error - No Parameters
```bash
curl "http://localhost:8080/movies/search"
```

**Response:**
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.",
  "treasures": []
}
```

---

### 3. Movie Details

**Endpoint:** `GET /movies/{id}/details`

**Description:** Returns detailed information about a specific movie treasure.

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Movie ID (path parameter) |

**Response:** HTML page with movie details and reviews

**Example:**
```bash
GET /movies/5/details
```

## Search Behavior

### Name Search
- **Case-insensitive**: "PRISON" matches "The Prison Escape"
- **Partial matching**: "Prison" matches "The Prison Escape"
- **Word boundaries**: Not enforced - "Pris" matches "The Prison Escape"

### ID Search
- **Exact matching**: Only returns movie with exact ID
- **Validation**: Must be positive integer
- **Range**: Valid IDs are 1-12 in the current dataset

### Genre Search
- **Case-insensitive**: "ACTION" matches "Action/Crime"
- **Partial matching**: "Action" matches "Action/Crime"
- **Multi-genre support**: Searches within combined genre strings

### Multiple Criteria
When multiple parameters are provided, they work as **AND** conditions:
- Movie must match ALL provided criteria
- Empty/null parameters are ignored
- Whitespace-only parameters are treated as empty

## Error Handling

### Client Errors (4xx)
- **400 Bad Request**: No search parameters provided
- **404 Not Found**: Invalid endpoint

### Server Errors (5xx)
- **500 Internal Server Error**: Unexpected server error during search

All errors include pirate-themed messages for a consistent user experience.

## Rate Limiting

Currently no rate limiting is implemented. Arrr, search to yer heart's content!

## Data Source

Movies are loaded from `src/main/resources/movies.json` containing 12 classic movies with the following genres:
- Drama
- Crime/Drama  
- Action/Crime
- Action/Sci-Fi
- Adventure/Fantasy
- Adventure/Sci-Fi
- Drama/Romance
- Drama/History
- Drama/Thriller

## Performance Considerations

- **In-memory search**: All movies loaded into memory for fast searching
- **No caching**: Results are computed on each request
- **Linear search**: O(n) complexity for filtering operations
- **Small dataset**: 12 movies, suitable for demonstration purposes

## SDK Examples

### JavaScript (Fetch API)
```javascript
// Search for action movies
async function searchMovies(name, id, genre) {
  const params = new URLSearchParams();
  if (name) params.append('name', name);
  if (id) params.append('id', id);
  if (genre) params.append('genre', genre);
  
  const response = await fetch(`/movies/search?${params}`);
  const data = await response.json();
  
  if (data.success) {
    console.log(`Found ${data.totalFound} treasures!`);
    return data.treasures;
  } else {
    console.error('Search failed:', data.message);
    return [];
  }
}

// Usage
const actionMovies = await searchMovies(null, null, 'action');
```

### Python (requests)
```python
import requests

def search_movies(name=None, movie_id=None, genre=None):
    params = {}
    if name:
        params['name'] = name
    if movie_id:
        params['id'] = movie_id
    if genre:
        params['genre'] = genre
    
    response = requests.get('http://localhost:8080/movies/search', params=params)
    data = response.json()
    
    if data['success']:
        print(f"Found {data['totalFound']} treasures!")
        return data['treasures']
    else:
        print(f"Search failed: {data['message']}")
        return []

# Usage
drama_movies = search_movies(genre='drama')
```

### Java (Spring RestTemplate)
```java
@Service
public class MovieSearchClient {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";
    
    public MovieSearchResponse searchMovies(String name, Long id, String genre) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(baseUrl + "/movies/search");
            
        if (name != null) builder.queryParam("name", name);
        if (id != null) builder.queryParam("id", id);
        if (genre != null) builder.queryParam("genre", genre);
        
        return restTemplate.getForObject(builder.toUriString(), MovieSearchResponse.class);
    }
}
```

## Testing the API

### Manual Testing with curl
```bash
# Test successful search
curl -X GET "http://localhost:8080/movies/search?name=adventure" \
  -H "Accept: application/json"

# Test error case
curl -X GET "http://localhost:8080/movies/search" \
  -H "Accept: application/json"

# Test multiple parameters
curl -X GET "http://localhost:8080/movies/search?name=the&genre=action" \
  -H "Accept: application/json"
```

### Automated Testing
The project includes comprehensive unit tests:
- `MovieServiceTest`: Tests search logic and edge cases
- `MoviesControllerTest`: Tests API endpoints and responses

Run tests with:
```bash
mvn test
```

## Changelog

### Version 1.1.0 (Current)
- ✅ Added movie search and filtering functionality
- ✅ Implemented REST API with JSON responses
- ✅ Enhanced HTML interface with search form
- ✅ Added pirate-themed language and UI
- ✅ Comprehensive unit test coverage
- ✅ Full documentation and examples

### Version 1.0.0 (Previous)
- Basic movie listing and details functionality

## Support

For issues, questions, or feature requests:
1. Check the main README.md for troubleshooting
2. Review the unit tests for usage examples
3. Examine the source code for implementation details

---

*"May the winds fill yer sails and yer API calls return successful, ye savvy developer!"* 🏴‍☠️⚓