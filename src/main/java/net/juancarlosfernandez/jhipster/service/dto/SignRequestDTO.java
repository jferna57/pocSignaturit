package net.juancarlosfernandez.jhipster.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SignRequest entity.
 */
public class SignRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private String bodyEmail;

    @NotNull
    private String subjectEmail;

    private String signaturitId;

    private String signaturitDocumentId;


    private Long contractId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getBodyEmail() {
        return bodyEmail;
    }

    public void setBodyEmail(String bodyEmail) {
        this.bodyEmail = bodyEmail;
    }
    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }
    public String getSignaturitId() {
        return signaturitId;
    }

    public void setSignaturitId(String signaturitId) {
        this.signaturitId = signaturitId;
    }
    public String getSignaturitDocumentId() {
        return signaturitDocumentId;
    }

    public void setSignaturitDocumentId(String signaturitDocumentId) {
        this.signaturitDocumentId = signaturitDocumentId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignRequestDTO signRequestDTO = (SignRequestDTO) o;

        if ( ! Objects.equals(id, signRequestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SignRequestDTO{" +
            "id=" + id +
            ", bodyEmail='" + bodyEmail + "'" +
            ", subjectEmail='" + subjectEmail + "'" +
            ", signaturitId='" + signaturitId + "'" +
            ", signaturitDocumentId='" + signaturitDocumentId + "'" +
            '}';
    }
}
