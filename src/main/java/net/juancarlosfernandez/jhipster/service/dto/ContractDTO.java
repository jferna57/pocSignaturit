package net.juancarlosfernandez.jhipster.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

import net.juancarlosfernandez.jhipster.domain.enumeration.Status;

/**
 * A DTO for the Contract entity.
 */
public class ContractDTO implements Serializable {

    private Long id;

    @NotNull
    private String contractName;

    @Lob
    private byte[] document;

    private String documentContentType;
    private Status status;

    @Lob
    private byte[] documentSigned;

    private String documentSignedContentType;
    private ZonedDateTime creationDate;

    private ZonedDateTime sentToBeSignedDate;

    private ZonedDateTime signedDate;


    private Long creatorId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public byte[] getDocumentSigned() {
        return documentSigned;
    }

    public void setDocumentSigned(byte[] documentSigned) {
        this.documentSigned = documentSigned;
    }

    public String getDocumentSignedContentType() {
        return documentSignedContentType;
    }

    public void setDocumentSignedContentType(String documentSignedContentType) {
        this.documentSignedContentType = documentSignedContentType;
    }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public ZonedDateTime getSentToBeSignedDate() {
        return sentToBeSignedDate;
    }

    public void setSentToBeSignedDate(ZonedDateTime sentToBeSignedDate) {
        this.sentToBeSignedDate = sentToBeSignedDate;
    }
    public ZonedDateTime getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(ZonedDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;

        if ( ! Objects.equals(id, contractDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + id +
            ", contractName='" + contractName + "'" +
            ", document='" + document + "'" +
            ", status='" + status + "'" +
            ", documentSigned='" + documentSigned + "'" +
            ", creationDate='" + creationDate + "'" +
            ", sentToBeSignedDate='" + sentToBeSignedDate + "'" +
            ", signedDate='" + signedDate + "'" +
            '}';
    }
}
