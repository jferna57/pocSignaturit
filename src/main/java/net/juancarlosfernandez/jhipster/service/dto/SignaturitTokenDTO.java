package net.juancarlosfernandez.jhipster.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the SignaturitToken entity.
 */
public class SignaturitTokenDTO implements Serializable {

    private Long id;

    @NotNull
    private String token;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignaturitTokenDTO signaturitTokenDTO = (SignaturitTokenDTO) o;

        if ( ! Objects.equals(id, signaturitTokenDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SignaturitTokenDTO{" +
            "id=" + id +
            ", token='" + token + "'" +
            '}';
    }
}
