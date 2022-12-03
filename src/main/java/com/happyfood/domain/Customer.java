package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Size(max = 50)
    @Column(name = "second_name", length = 50)
    private String secondName;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_lastname", length = 50, nullable = false)
    private String firstLastname;

    @Size(max = 50)
    @Column(name = "second_lastname", length = 50)
    private String secondLastname;

    @NotNull
    @Size(max = 100)
    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @NotNull
    @Size(max = 50)
    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @NotNull
    @Size(max = 50)
    @Column(name = "document_number", length = 50, nullable = false, unique = true)
    private String documentNumber;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = { "invoices", "customer" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customers" }, allowSetters = true)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public Customer secondName(String secondName) {
        this.setSecondName(secondName);
        return this;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstLastname() {
        return this.firstLastname;
    }

    public Customer firstLastname(String firstLastname) {
        this.setFirstLastname(firstLastname);
        return this;
    }

    public void setFirstLastname(String firstLastname) {
        this.firstLastname = firstLastname;
    }

    public String getSecondLastname() {
        return this.secondLastname;
    }

    public Customer secondLastname(String secondLastname) {
        this.setSecondLastname(secondLastname);
        return this;
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname = secondLastname;
    }

    public String getAddress() {
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public Customer documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCustomer(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCustomer(this));
        }
        this.orders = orders;
    }

    public Customer orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Customer addOrder(Order order) {
        this.orders.add(order);
        order.setCustomer(this);
        return this;
    }

    public Customer removeOrder(Order order) {
        this.orders.remove(order);
        order.setCustomer(null);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Customer documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", firstLastname='" + getFirstLastname() + "'" +
            ", secondLastname='" + getSecondLastname() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            "}";
    }
}
