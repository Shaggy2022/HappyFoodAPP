package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.happyfood.domain.enumeration.State;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DocumentType.
 */
@Entity
@Table(name = "document_type")
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "initials", length = 10, nullable = false, unique = true)
    private String initials;

    @NotNull
    @Size(max = 100)
    @Column(name = "document_name", length = 100, nullable = false, unique = true)
    private String documentName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_document_type", nullable = false)
    private State stateDocumentType;

    @OneToMany(mappedBy = "documentType")
    @JsonIgnoreProperties(value = { "user", "orders", "documentType" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitials() {
        return this.initials;
    }

    public DocumentType initials(String initials) {
        this.setInitials(initials);
        return this;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public DocumentType documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public State getStateDocumentType() {
        return this.stateDocumentType;
    }

    public DocumentType stateDocumentType(State stateDocumentType) {
        this.setStateDocumentType(stateDocumentType);
        return this;
    }

    public void setStateDocumentType(State stateDocumentType) {
        this.stateDocumentType = stateDocumentType;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setDocumentType(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setDocumentType(this));
        }
        this.customers = customers;
    }

    public DocumentType customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public DocumentType addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setDocumentType(this);
        return this;
    }

    public DocumentType removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setDocumentType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", initials='" + getInitials() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", stateDocumentType='" + getStateDocumentType() + "'" +
            "}";
    }
}
