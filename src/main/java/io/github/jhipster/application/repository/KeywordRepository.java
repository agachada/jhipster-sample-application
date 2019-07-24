package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Keyword;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Keyword entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeywordRepository extends MongoRepository<Keyword, String> {

}
