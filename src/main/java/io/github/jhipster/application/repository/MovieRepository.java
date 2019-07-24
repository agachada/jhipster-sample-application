package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Movie entity.
 */
@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    @Query("{}")
    Page<Movie> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Movie> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Movie> findOneWithEagerRelationships(String id);

}
