package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Movie;
import io.github.jhipster.application.repository.MovieRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.Movie}.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private static final String ENTITY_NAME = "movie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovieRepository movieRepository;

    public MovieResource(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * {@code POST  /movies} : Create a new movie.
     *
     * @param movie the movie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movie, or with status {@code 400 (Bad Request)} if the movie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movie);
        if (movie.getId() != null) {
            throw new BadRequestAlertException("A new movie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Movie result = movieRepository.save(movie);
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movies} : Updates an existing movie.
     *
     * @param movie the movie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movie,
     * or with status {@code 400 (Bad Request)} if the movie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movies")
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movie);
        if (movie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Movie result = movieRepository.save(movie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, movie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /movies} : get all the movies.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movies in body.
     */
    @GetMapping("/movies")
    public List<Movie> getAllMovies(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Movies");
        return movieRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /movies/:id} : get the "id" movie.
     *
     * @param id the id of the movie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable String id) {
        log.debug("REST request to get Movie : {}", id);
        Optional<Movie> movie = movieRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(movie);
    }

    /**
     * {@code DELETE  /movies/:id} : delete the "id" movie.
     *
     * @param id the id of the movie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        log.debug("REST request to delete Movie : {}", id);
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
