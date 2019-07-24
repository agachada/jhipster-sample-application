package io.github.jhipster.application.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Movie.
 */
@Document(collection = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @DBRef
    @Field("ids")
    private Set<Keyword> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Keyword> getIds() {
        return ids;
    }

    public Movie ids(Set<Keyword> keywords) {
        this.ids = keywords;
        return this;
    }

    public Movie addId(Keyword keyword) {
        this.ids.add(keyword);
        keyword.getIds().add(this);
        return this;
    }

    public Movie removeId(Keyword keyword) {
        this.ids.remove(keyword);
        keyword.getIds().remove(this);
        return this;
    }

    public void setIds(Set<Keyword> keywords) {
        this.ids = keywords;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
