package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! This method hunts for treasure (movies) based on search criteria.
     * Searches through our movie treasure chest using name, id, and genre filters.
     * 
     * @param treasureName The name of the movie treasure to search for (partial match, case-insensitive)
     * @param treasureId The specific ID of the movie treasure
     * @param treasureGenre The genre of movie treasures to hunt for (partial match, case-insensitive)
     * @return List of movie treasures that match the search criteria, arrr!
     */
    public List<Movie> huntForMovieTreasures(String treasureName, Long treasureId, String treasureGenre) {
        logger.info("Ahoy! Starting treasure hunt with name: '{}', id: {}, genre: '{}'", 
                   treasureName, treasureId, treasureGenre);
        
        List<Movie> treasureHaul = new ArrayList<>(movies);
        
        // Filter by treasure name if provided, arrr!
        if (treasureName != null && !treasureName.trim().isEmpty()) {
            String searchName = treasureName.trim().toLowerCase();
            treasureHaul = treasureHaul.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
            logger.debug("Filtered by name '{}', found {} treasures", searchName, treasureHaul.size());
        }
        
        // Filter by treasure ID if provided, matey!
        if (treasureId != null && treasureId > 0) {
            treasureHaul = treasureHaul.stream()
                .filter(movie -> movie.getId() == treasureId)
                .collect(Collectors.toList());
            logger.debug("Filtered by ID {}, found {} treasures", treasureId, treasureHaul.size());
        }
        
        // Filter by treasure genre if provided, ye scurvy dog!
        if (treasureGenre != null && !treasureGenre.trim().isEmpty()) {
            String searchGenre = treasureGenre.trim().toLowerCase();
            treasureHaul = treasureHaul.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(Collectors.toList());
            logger.debug("Filtered by genre '{}', found {} treasures", searchGenre, treasureHaul.size());
        }
        
        logger.info("Treasure hunt complete! Found {} movie treasures", treasureHaul.size());
        return treasureHaul;
    }

    /**
     * Arrr! Get all unique genres from our treasure chest of movies.
     * Useful for showing available genres to search through, matey!
     * 
     * @return List of all unique genres in our movie collection
     */
    public List<String> getAllTreasureGenres() {
        return movies.stream()
            .map(Movie::getGenre)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
}
