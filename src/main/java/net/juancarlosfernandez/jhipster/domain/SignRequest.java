package net.juancarlosfernandez.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SignRequest.
 */
@Entity
@Table(name = "sign_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SignRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "body_email", nullable = false)
    private String bodyEmail;

    @NotNull
    @Column(name = "subject_email", nullable = false)
    private String subjectEmail;

    @Column(name = "signaturit_id")
    private String signaturitId;

    @Column(name = "signaturit_document_id")
    private String signaturitDocumentId;

    @OneToOne
    @JoinColumn(unique = true)
    private Contract contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBodyEmail() {
        return bodyEmail;
    }

    public SignRequest bodyEmail(String bodyEmail) {
        this.bodyEmail = bodyEmail;
        return this;
    }

    public void setBodyEmail(String bodyEmail) {
        this.bodyEmail = bodyEmail;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public SignRequest subjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
        return this;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getSignaturitId() {
        return signaturitId;
    }

    public SignRequest signaturitId(String signaturitId) {
        this.signaturitId = signaturitId;
        return this;
    }

    public void setSignaturitId(String signaturitId) {
        this.signaturitId = signaturitId;
    }

    public String getSignaturitDocumentId() {
        return signaturitDocumentId;
    }

    public SignRequest signaturitDocumentId(String signaturitDocumentId) {
        this.signaturitDocumentId = signaturitDocumentId;
        return this;
    }

    public void setSignaturitDocumentId(String signaturitDocumentId) {
        this.signaturitDocumentId = signaturitDocumentId;
    }

    public Contract getContract() {
        return contract;
    }

    public SignRequest contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignRequest signRequest = (SignRequest) o;
        if(signRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, signRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SignRequest{" +
            "id=" + id +
            ", bodyEmail='" + bodyEmail + "'" +
            ", subjectEmail='" + subjectEmail + "'" +
            ", signaturitId='" + signaturitId + "'" +
            ", signaturitDocumentId='" + signaturitDocumentId + "'" +
            '}';
    }
}
