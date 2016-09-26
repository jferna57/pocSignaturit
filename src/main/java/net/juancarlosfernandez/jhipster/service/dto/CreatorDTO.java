package net.juancarlosfernandez.jhipster.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Creator entity.
 */
public class CreatorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CreatorDTO creatorDTO = (CreatorDTO) o;

        if ( ! Objects.equals(id, creatorDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CreatorDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
