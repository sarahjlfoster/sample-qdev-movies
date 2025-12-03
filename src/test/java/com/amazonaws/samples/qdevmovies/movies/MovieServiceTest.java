package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arrr! Unit tests for the MovieService treasure hunting functionality, matey!
 * These tests ensure our movie search capabilities work ship-shape.
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Ahoy! Test getting all movie treasures")
    public void testGetAllMovies() {
        List<Movie> allTreasures = movieService.getAllMovies();
        
        assertNotNull(allTreasures, "Movie treasure chest should not be null, ye scallywag!");
        assertFalse(allTreasures.isEmpty(), "Treasure chest should not be empty, arrr!");
        
        // Verify we have the expected number of movies from movies.json
        assertEquals(12, allTreasures.size(), "Should have 12 movie treasures in the chest!");
    }

    @Test
    @DisplayName("Treasure hunt by movie name - exact match")
    public void testHuntForMovieTreasuresByName_ExactMatch() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("The Prison Escape", null, null);
        
        assertEquals(1, treasureHaul.size(), "Should find exactly one treasure, matey!");
        assertEquals("The Prison Escape", treasureHaul.get(0).getMovieName());
        assertEquals(1L, treasureHaul.get(0).getId());
    }

    @Test
    @DisplayName("Treasure hunt by movie name - partial match")
    public void testHuntForMovieTreasuresByName_PartialMatch() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("The", null, null);
        
        assertTrue(treasureHaul.size() > 1, "Should find multiple treasures with 'The' in the name, arrr!");
        
        // Verify all results contain "The" in the name
        for (Movie treasure : treasureHaul) {
            assertTrue(treasure.getMovieName().toLowerCase().contains("the"), 
                      "All treasures should contain 'the' in the name: " + treasure.getMovieName());
        }
    }

    @Test
    @DisplayName("Treasure hunt by movie name - case insensitive")
    public void testHuntForMovieTreasuresByName_CaseInsensitive() {
        List<Movie> treasureHaul1 = movieService.huntForMovieTreasures("prison", null, null);
        List<Movie> treasureHaul2 = movieService.huntForMovieTreasures("PRISON", null, null);
        List<Movie> treasureHaul3 = movieService.huntForMovieTreasures("Prison", null, null);
        
        assertEquals(1, treasureHaul1.size(), "Should find treasure regardless of case, ye landlubber!");
        assertEquals(treasureHaul1.size(), treasureHaul2.size(), "Case should not matter, arrr!");
        assertEquals(treasureHaul1.size(), treasureHaul3.size(), "Case should not matter, savvy!");
        
        assertEquals("The Prison Escape", treasureHaul1.get(0).getMovieName());
    }

    @Test
    @DisplayName("Treasure hunt by ID - valid ID")
    public void testHuntForMovieTreasuresById_ValidId() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures(null, 5L, null);
        
        assertEquals(1, treasureHaul.size(), "Should find exactly one treasure by ID, matey!");
        assertEquals(5L, treasureHaul.get(0).getId());
        assertEquals("Life Journey", treasureHaul.get(0).getMovieName());
    }

    @Test
    @DisplayName("Treasure hunt by ID - invalid ID")
    public void testHuntForMovieTreasuresById_InvalidId() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures(null, 999L, null);
        
        assertTrue(treasureHaul.isEmpty(), "Should find no treasures for invalid ID, ye scurvy dog!");
    }

    @Test
    @DisplayName("Treasure hunt by genre - exact match")
    public void testHuntForMovieTreasuresByGenre_ExactMatch() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures(null, null, "Drama");
        
        assertTrue(treasureHaul.size() > 0, "Should find drama treasures, arrr!");
        
        // Verify all results contain "Drama" in the genre
        for (Movie treasure : treasureHaul) {
            assertTrue(treasure.getGenre().toLowerCase().contains("drama"), 
                      "All treasures should be drama genre: " + treasure.getGenre());
        }
    }

    @Test
    @DisplayName("Treasure hunt by genre - partial match")
    public void testHuntForMovieTreasuresByGenre_PartialMatch() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures(null, null, "Sci");
        
        assertTrue(treasureHaul.size() > 0, "Should find sci-fi treasures, matey!");
        
        // Verify all results contain "Sci" in the genre
        for (Movie treasure : treasureHaul) {
            assertTrue(treasure.getGenre().toLowerCase().contains("sci"), 
                      "All treasures should contain 'sci' in genre: " + treasure.getGenre());
        }
    }

    @Test
    @DisplayName("Treasure hunt by genre - case insensitive")
    public void testHuntForMovieTreasuresByGenre_CaseInsensitive() {
        List<Movie> treasureHaul1 = movieService.huntForMovieTreasures(null, null, "action");
        List<Movie> treasureHaul2 = movieService.huntForMovieTreasures(null, null, "ACTION");
        List<Movie> treasureHaul3 = movieService.huntForMovieTreasures(null, null, "Action");
        
        assertEquals(treasureHaul1.size(), treasureHaul2.size(), "Case should not matter for genre search, arrr!");
        assertEquals(treasureHaul1.size(), treasureHaul3.size(), "Case should not matter for genre search, savvy!");
    }

    @Test
    @DisplayName("Treasure hunt with multiple criteria")
    public void testHuntForMovieTreasures_MultipleCriteria() {
        // Search for movies with "The" in name and "Action" in genre
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("The", null, "Action");
        
        assertTrue(treasureHaul.size() > 0, "Should find treasures matching both criteria, ye landlubber!");
        
        // Verify all results match both criteria
        for (Movie treasure : treasureHaul) {
            assertTrue(treasure.getMovieName().toLowerCase().contains("the"), 
                      "Treasure name should contain 'the': " + treasure.getMovieName());
            assertTrue(treasure.getGenre().toLowerCase().contains("action"), 
                      "Treasure genre should contain 'action': " + treasure.getGenre());
        }
    }

    @Test
    @DisplayName("Treasure hunt with all criteria")
    public void testHuntForMovieTreasures_AllCriteria() {
        // This should find "The Masked Hero" (ID 3, Action/Crime genre)
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("Masked", 3L, "Action");
        
        assertEquals(1, treasureHaul.size(), "Should find exactly one treasure matching all criteria, matey!");
        
        Movie treasure = treasureHaul.get(0);
        assertEquals(3L, treasure.getId());
        assertEquals("The Masked Hero", treasure.getMovieName());
        assertTrue(treasure.getGenre().contains("Action"));
    }

    @Test
    @DisplayName("Treasure hunt with no matching results")
    public void testHuntForMovieTreasures_NoResults() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("NonexistentMovie", null, null);
        
        assertTrue(treasureHaul.isEmpty(), "Should find no treasures for non-existent movie, arrr!");
    }

    @Test
    @DisplayName("Treasure hunt with empty/null parameters")
    public void testHuntForMovieTreasures_EmptyParameters() {
        List<Movie> treasureHaul1 = movieService.huntForMovieTreasures("", null, "");
        List<Movie> treasureHaul2 = movieService.huntForMovieTreasures(null, null, null);
        List<Movie> allTreasures = movieService.getAllMovies();
        
        assertEquals(allTreasures.size(), treasureHaul1.size(), 
                    "Empty parameters should return all treasures, ye scallywag!");
        assertEquals(allTreasures.size(), treasureHaul2.size(), 
                    "Null parameters should return all treasures, matey!");
    }

    @Test
    @DisplayName("Treasure hunt with whitespace-only parameters")
    public void testHuntForMovieTreasures_WhitespaceParameters() {
        List<Movie> treasureHaul = movieService.huntForMovieTreasures("   ", null, "  ");
        List<Movie> allTreasures = movieService.getAllMovies();
        
        assertEquals(allTreasures.size(), treasureHaul.size(), 
                    "Whitespace-only parameters should return all treasures, arrr!");
    }

    @Test
    @DisplayName("Get all treasure genres")
    public void testGetAllTreasureGenres() {
        List<String> genres = movieService.getAllTreasureGenres();
        
        assertNotNull(genres, "Genres list should not be null, ye landlubber!");
        assertFalse(genres.isEmpty(), "Should have genres available, matey!");
        
        // Verify genres are unique and sorted
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i-1).compareTo(genres.get(i)) < 0, 
                      "Genres should be sorted alphabetically, arrr!");
        }
        
        // Verify some expected genres exist
        assertTrue(genres.contains("Drama"), "Should contain Drama genre, savvy!");
        assertTrue(genres.contains("Action/Crime"), "Should contain Action/Crime genre, matey!");
    }

    @Test
    @DisplayName("Test individual movie properties")
    public void testMovieProperties() {
        List<Movie> allTreasures = movieService.getAllMovies();
        Movie firstTreasure = allTreasures.get(0);
        
        // Verify the first movie has expected properties
        assertNotNull(firstTreasure.getMovieName(), "Movie name should not be null, arrr!");
        assertNotNull(firstTreasure.getDirector(), "Director should not be null, matey!");
        assertNotNull(firstTreasure.getGenre(), "Genre should not be null, ye scallywag!");
        assertNotNull(firstTreasure.getDescription(), "Description should not be null, savvy!");
        
        assertTrue(firstTreasure.getId() > 0, "Movie ID should be positive, arrr!");
        assertTrue(firstTreasure.getYear() > 1800, "Movie year should be reasonable, matey!");
        assertTrue(firstTreasure.getDuration() > 0, "Movie duration should be positive, ye landlubber!");
        assertTrue(firstTreasure.getImdbRating() >= 0 && firstTreasure.getImdbRating() <= 5, 
                  "IMDB rating should be between 0 and 5, savvy!");
    }
}