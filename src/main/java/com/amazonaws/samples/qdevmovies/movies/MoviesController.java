package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model,
                           @RequestParam(value = "name", required = false) String treasureName,
                           @RequestParam(value = "id", required = false) Long treasureId,
                           @RequestParam(value = "genre", required = false) String treasureGenre) {
        logger.info("Ahoy! Fetching movies with search parameters - name: '{}', id: {}, genre: '{}'", 
                   treasureName, treasureId, treasureGenre);
        
        List<Movie> movieTreasures;
        boolean isSearching = (treasureName != null && !treasureName.trim().isEmpty()) ||
                             (treasureId != null && treasureId > 0) ||
                             (treasureGenre != null && !treasureGenre.trim().isEmpty());
        
        if (isSearching) {
            // Arrr! Time for a treasure hunt!
            movieTreasures = movieService.huntForMovieTreasures(treasureName, treasureId, treasureGenre);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", treasureName);
            model.addAttribute("searchId", treasureId);
            model.addAttribute("searchGenre", treasureGenre);
            
            if (movieTreasures.isEmpty()) {
                model.addAttribute("noTreasuresFound", true);
                model.addAttribute("pirateMessage", "Shiver me timbers! No movie treasures found matching yer search criteria, matey!");
            } else {
                model.addAttribute("pirateMessage", 
                    String.format("Ahoy! Found %d movie treasure%s in yer search, ye savvy sailor!", 
                                movieTreasures.size(), movieTreasures.size() == 1 ? "" : "s"));
            }
        } else {
            // Show all treasures in the chest, arrr!
            movieTreasures = movieService.getAllMovies();
            model.addAttribute("searchPerformed", false);
            model.addAttribute("pirateMessage", "Welcome to our treasure chest of free movies this month, matey!");
        }
        
        model.addAttribute("movies", movieTreasures);
        model.addAttribute("availableGenres", movieService.getAllTreasureGenres());
        return "movies";
    }

    /**
     * Arrr! REST API endpoint for treasure hunting (movie searching).
     * Returns JSON response with search results, perfect for AJAX calls or API consumers.
     * 
     * @param treasureName Movie name to search for (partial match)
     * @param treasureId Specific movie ID to find
     * @param treasureGenre Genre to filter by (partial match)
     * @return ResponseEntity with search results and pirate-themed messages
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMovieTreasures(
            @RequestParam(value = "name", required = false) String treasureName,
            @RequestParam(value = "id", required = false) Long treasureId,
            @RequestParam(value = "genre", required = false) String treasureGenre) {
        
        logger.info("Ahoy! API treasure hunt requested with name: '{}', id: {}, genre: '{}'", 
                   treasureName, treasureId, treasureGenre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate search parameters, ye scallywag!
            if ((treasureName == null || treasureName.trim().isEmpty()) &&
                (treasureId == null || treasureId <= 0) &&
                (treasureGenre == null || treasureGenre.trim().isEmpty())) {
                
                response.put("success", false);
                response.put("message", "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.");
                response.put("treasures", List.of());
                return ResponseEntity.badRequest().body(response);
            }
            
            // Hunt for movie treasures!
            List<Movie> treasureHaul = movieService.huntForMovieTreasures(treasureName, treasureId, treasureGenre);
            
            response.put("success", true);
            response.put("treasures", treasureHaul);
            response.put("totalFound", treasureHaul.size());
            
            if (treasureHaul.isEmpty()) {
                response.put("message", "Shiver me timbers! No movie treasures found matching yer search criteria. Try different search terms, ye landlubber!");
            } else {
                response.put("message", 
                    String.format("Ahoy! Successfully found %d movie treasure%s matching yer search, captain!", 
                                treasureHaul.size(), treasureHaul.size() == 1 ? "" : "s"));
            }
            
            // Add search parameters to response for reference
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("name", treasureName);
            searchParams.put("id", treasureId);
            searchParams.put("genre", treasureGenre);
            response.put("searchParameters", searchParams);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Arrr! Treasure hunt failed with error: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Blimey! Something went wrong during the treasure hunt. Try again later, matey!");
            response.put("treasures", List.of());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}