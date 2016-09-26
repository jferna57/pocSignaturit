package net.juancarlosfernandez.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import net.juancarlosfernandez.jhipster.domain.enumeration.Status;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "contract_name", nullable = false)
    private String contractName;

    @Lob
    @Column(name = "document")
    private byte[] document;

    @Column(name = "document_content_type")
    private String documentContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Lob
    @Column(name = "document_signed")
    private byte[] documentSigned;

    @Column(name = "document_signed_content_type")
    private String documentSignedContentType;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "sent_to_be_signed_date")
    private ZonedDateTime sentToBeSignedDate;

    @Column(name = "signed_date")
    private ZonedDateTime signedDate;

    @OneToMany(mappedBy = "contract")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Signer> signers = new HashSet<>();

    @ManyToOne
    private Creator creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractName() {
        return contractName;
    }

    public Contract contractName(String contractName) {
        this.contractName = contractName;
        return this;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public byte[] getDocument() {
        return document;
    }

    public Contract document(byte[] document) {
        this.document = document;
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Contract documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Status getStatus() {
        return status;
    }

    public Contract status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getDocumentSigned() {
        return documentSigned;
    }

    public Contract documentSigned(byte[] documentSigned) {
        this.documentSigned = documentSigned;
        return this;
    }

    public void setDocumentSigned(byte[] documentSigned) {
        this.documentSigned = documentSigned;
    }

    public String getDocumentSignedContentType() {
        return documentSignedContentType;
    }

    public Contract documentSignedContentType(String documentSignedContentType) {
        this.documentSignedContentType = documentSignedContentType;
        return this;
    }

    public void setDocumentSignedContentType(String documentSignedContentType) {
        this.documentSignedContentType = documentSignedContentType;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Contract creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getSentToBeSignedDate() {
        return sentToBeSignedDate;
    }

    public Contract sentToBeSignedDate(ZonedDateTime sentToBeSignedDate) {
        this.sentToBeSignedDate = sentToBeSignedDate;
        return this;
    }

    public void setSentToBeSignedDate(ZonedDateTime sentToBeSignedDate) {
        this.sentToBeSignedDate = sentToBeSignedDate;
    }

    public ZonedDateTime getSignedDate() {
        return signedDate;
    }

    public Contract signedDate(ZonedDateTime signedDate) {
        this.signedDate = signedDate;
        return this;
    }

    public void setSignedDate(ZonedDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public Set<Signer> getSigners() {
        return signers;
    }

    public Contract signers(Set<Signer> signers) {
        this.signers = signers;
        return this;
    }

    public Contract addSigner(Signer signer) {
        signers.add(signer);
        signer.setContract(this);
        return this;
    }

    public Contract removeSigner(Signer signer) {
        signers.remove(signer);
        signer.setContract(null);
        return this;
    }

    public void setSigners(Set<Signer> signers) {
        this.signers = signers;
    }

    public Creator getCreator() {
        return creator;
    }

    public Contract creator(Creator creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if(contract.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + id +
            ", contractName='" + contractName + "'" +
            ", document='" + document + "'" +
            ", documentContentType='" + documentContentType + "'" +
            ", status='" + status + "'" +
            ", documentSigned='" + documentSigned + "'" +
            ", documentSignedContentType='" + documentSignedContentType + "'" +
            ", creationDate='" + creationDate + "'" +
            ", sentToBeSignedDate='" + sentToBeSignedDate + "'" +
            ", signedDate='" + signedDate + "'" +
            '}';
    }
}
