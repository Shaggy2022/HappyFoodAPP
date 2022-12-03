package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Order.
 */
@Entity
@Table(name = "happy_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "order_required", length = 200, nullable = false)
    private String orderRequired;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "full_order_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal fullOrderValue;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "invoiceProducts", "inventories", "order" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "orders", "documentType" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderRequired() {
        return this.orderRequired;
    }

    public Order orderRequired(String orderRequired) {
        this.setOrderRequired(orderRequired);
        return this;
    }

    public void setOrderRequired(String orderRequired) {
        this.orderRequired = orderRequired;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Order date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Order amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getFullOrderValue() {
        return this.fullOrderValue;
    }

    public Order fullOrderValue(BigDecimal fullOrderValue) {
        this.setFullOrderValue(fullOrderValue);
        return this;
    }

    public void setFullOrderValue(BigDecimal fullOrderValue) {
        this.fullOrderValue = fullOrderValue;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setOrder(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setOrder(this));
        }
        this.invoices = invoices;
    }

    public Order invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Order addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setOrder(this);
        return this;
    }

    public Order removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setOrder(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderRequired='" + getOrderRequired() + "'" +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            ", fullOrderValue=" + getFullOrderValue() +
            "}";
    }
}
