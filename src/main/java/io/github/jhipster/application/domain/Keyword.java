package io.github.jhipster.application.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Keyword.
 */
@Document(collection = "keyword")
public class Keyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @DBRef
    @Field("ids")
    @JsonIgnore
    private Set<Movie> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Keyword name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getIds() {
        return ids;
    }

    public Keyword ids(Set<Movie> movies) {
        this.ids = movies;
        return this;
    }

    public Keyword addId(Movie movie) {
        this.ids.add(movie);
        movie.getIds().add(this);
        return this;
    }

    public Keyword removeId(Movie movie) {
        this.ids.remove(movie);
        movie.getIds().remove(this);
        return this;
    }

    public void setIds(Set<Movie> movies) {
        this.ids = movies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Keyword)) {
            return false;
        }
        return id != null && id.equals(((Keyword) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Keyword{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
