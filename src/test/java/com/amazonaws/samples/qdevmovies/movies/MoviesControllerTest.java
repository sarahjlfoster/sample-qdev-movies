package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arrr! Unit tests for the MoviesController treasure hunting endpoints, matey!
 * These tests ensure our movie search API works like a well-oiled pirate ship.
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-themed test data, arrr!
        mockMovieService = new MockMovieService();
        mockReviewService = new MockReviewService();
        
        // Inject mocks using reflection (ahoy, this be the way!)
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services, ye scurvy dog!", e);
        }
    }

    @Test
    @DisplayName("Ahoy! Test getting all movies without search parameters")
    public void testGetMovies_NoSearchParameters() {
        String result = moviesController.getMovies(model, null, null, null);
        
        assertEquals("movies", result, "Should return movies template, matey!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null, arrr!");
        assertEquals(3, movies.size(), "Should return all test movies, ye landlubber!");
        
        assertFalse((Boolean) model.getAttribute("searchPerformed"), "Search should not be performed, savvy!");
        assertEquals("Welcome to our treasure chest of free movies this month, matey!", 
                    model.getAttribute("pirateMessage"));
    }

    @Test
    @DisplayName("Treasure hunt by movie name")
    public void testGetMovies_SearchByName() {
        String result = moviesController.getMovies(model, "Pirate", null, null);
        
        assertEquals("movies", result, "Should return movies template, arrr!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null, matey!");
        assertEquals(1, movies.size(), "Should find one pirate movie, ye scallywag!");
        assertEquals("Pirate Adventure", movies.get(0).getMovieName());
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search should be performed, savvy!");
        assertEquals("Pirate", model.getAttribute("searchName"));
        assertNull(model.getAttribute("searchId"));
        assertNull(model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Treasure hunt by movie ID")
    public void testGetMovies_SearchById() {
        String result = moviesController.getMovies(model, null, 2L, null);
        
        assertEquals("movies", result, "Should return movies template, matey!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null, arrr!");
        assertEquals(1, movies.size(), "Should find one movie by ID, ye landlubber!");
        assertEquals(2L, movies.get(0).getId());
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search should be performed, savvy!");
        assertEquals(2L, model.getAttribute("searchId"));
    }

    @Test
    @DisplayName("Treasure hunt by genre")
    public void testGetMovies_SearchByGenre() {
        String result = moviesController.getMovies(model, null, null, "Adventure");
        
        assertEquals("movies", result, "Should return movies template, arrr!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null, matey!");
        assertEquals(2, movies.size(), "Should find two adventure movies, ye scallywag!");
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search should be performed, savvy!");
        assertEquals("Adventure", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Treasure hunt with no results")
    public void testGetMovies_NoResults() {
        String result = moviesController.getMovies(model, "NonexistentMovie", null, null);
        
        assertEquals("movies", result, "Should return movies template, arrr!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should not be null, matey!");
        assertTrue(movies.isEmpty(), "Should find no movies, ye landlubber!");
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search should be performed, savvy!");
        assertTrue((Boolean) model.getAttribute("noTreasuresFound"), "No treasures flag should be set, arrr!");
        assertEquals("Shiver me timbers! No movie treasures found matching yer search criteria, matey!", 
                    model.getAttribute("pirateMessage"));
    }

    @Test
    @DisplayName("REST API treasure hunt - valid search")
    public void testSearchMovieTreasures_ValidSearch() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMovieTreasures("Pirate", null, null);
        
        assertEquals(200, response.getStatusCodeValue(), "Should return OK status, matey!");
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null, arrr!");
        assertTrue((Boolean) body.get("success"), "Search should be successful, ye scallywag!");
        assertEquals(1, body.get("totalFound"), "Should find one treasure, savvy!");
        
        @SuppressWarnings("unchecked")
        List<Movie> treasures = (List<Movie>) body.get("treasures");
        assertEquals(1, treasures.size(), "Should return one treasure, matey!");
        assertEquals("Pirate Adventure", treasures.get(0).getMovieName());
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Ahoy!"), "Message should be pirate-themed, arrr!");
    }

    @Test
    @DisplayName("REST API treasure hunt - no parameters")
    public void testSearchMovieTreasures_NoParameters() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMovieTreasures(null, null, null);
        
        assertEquals(400, response.getStatusCodeValue(), "Should return bad request status, ye scallywag!");
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null, arrr!");
        assertFalse((Boolean) body.get("success"), "Search should not be successful, matey!");
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Arrr!"), "Error message should be pirate-themed, savvy!");
        assertTrue(message.contains("at least one search parameter"), "Should mention missing parameters, ye landlubber!");
    }

    @Test
    @DisplayName("Test movie details endpoint")
    public void testGetMovieDetails_ValidId() {
        String result = moviesController.getMovieDetails(1L, model);
        
        assertEquals("movie-details", result, "Should return movie-details template, matey!");
        
        Movie movie = (Movie) model.getAttribute("movie");
        assertNotNull(movie, "Movie should not be null, arrr!");
        assertEquals(1L, movie.getId(), "Should return correct movie, ye scallywag!");
    }

    @Test
    @DisplayName("Test movie details endpoint - not found")
    public void testGetMovieDetails_NotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        
        assertEquals("error", result, "Should return error template, matey!");
        
        String title = (String) model.getAttribute("title");
        String message = (String) model.getAttribute("message");
        assertEquals("Movie Not Found", title, "Should set error title, arrr!");
        assertTrue(message.contains("999"), "Error message should mention the ID, ye landlubber!");
    }

    // Mock MovieService for testing, arrr!
    private static class MockMovieService extends MovieService {
        private final List<Movie> testMovies;
        private final List<String> testGenres;

        public MockMovieService() {
            this.testMovies = Arrays.asList(
                new Movie(1L, "Pirate Adventure", "Captain Hook", 2023, "Adventure", "A swashbuckling tale", 120, 4.5),
                new Movie(2L, "Treasure Island", "Long John Silver", 2022, "Adventure/Drama", "Hunt for buried treasure", 110, 4.0),
                new Movie(3L, "Space Odyssey", "Stanley Kubrick", 2001, "Sci-Fi", "A journey through space", 150, 5.0)
            );
            this.testGenres = Arrays.asList("Adventure", "Adventure/Drama", "Sci-Fi");
        }

        @Override
        public List<Movie> getAllMovies() {
            return new ArrayList<>(testMovies);
        }

        @Override
        public Optional<Movie> getMovieById(Long id) {
            return testMovies.stream()
                .filter(movie -> movie.getId() == id)
                .findFirst();
        }

        @Override
        public List<Movie> huntForMovieTreasures(String treasureName, Long treasureId, String treasureGenre) {
            List<Movie> results = new ArrayList<>(testMovies);

            if (treasureName != null && !treasureName.trim().isEmpty()) {
                String searchName = treasureName.trim().toLowerCase();
                results = results.stream()
                    .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                    .collect(java.util.stream.Collectors.toList());
            }

            if (treasureId != null && treasureId > 0) {
                results = results.stream()
                    .filter(movie -> movie.getId() == treasureId)
                    .collect(java.util.stream.Collectors.toList());
            }

            if (treasureGenre != null && !treasureGenre.trim().isEmpty()) {
                String searchGenre = treasureGenre.trim().toLowerCase();
                results = results.stream()
                    .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                    .collect(java.util.stream.Collectors.toList());
            }

            return results;
        }

        @Override
        public List<String> getAllTreasureGenres() {
            return new ArrayList<>(testGenres);
        }
    }

    // Mock ReviewService for testing, savvy!
    private static class MockReviewService extends ReviewService {
        @Override
        public List<Review> getReviewsForMovie(long movieId) {
            return new ArrayList<>();
        }
    }
}
