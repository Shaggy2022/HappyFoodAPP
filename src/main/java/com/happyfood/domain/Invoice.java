package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false, unique = true)
    private Integer invoiceNumber;

    @NotNull
    @Column(name = "iva", nullable = false)
    private Double iva;

    @NotNull
    @Column(name = "total_to_pay", nullable = false)
    private Double totalToPay;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "product", "invoice" }, allowSetters = true)
    private Set<InvoiceProduct> invoiceProducts = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "products", "invoice" }, allowSetters = true)
    private Set<Inventory> inventories = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "invoices", "customer" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(Integer invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getIva() {
        return this.iva;
    }

    public Invoice iva(Double iva) {
        this.setIva(iva);
        return this;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotalToPay() {
        return this.totalToPay;
    }

    public Invoice totalToPay(Double totalToPay) {
        this.setTotalToPay(totalToPay);
        return this;
    }

    public void setTotalToPay(Double totalToPay) {
        this.totalToPay = totalToPay;
    }

    public Set<InvoiceProduct> getInvoiceProducts() {
        return this.invoiceProducts;
    }

    public void setInvoiceProducts(Set<InvoiceProduct> invoiceProducts) {
        if (this.invoiceProducts != null) {
            this.invoiceProducts.forEach(i -> i.setInvoice(null));
        }
        if (invoiceProducts != null) {
            invoiceProducts.forEach(i -> i.setInvoice(this));
        }
        this.invoiceProducts = invoiceProducts;
    }

    public Invoice invoiceProducts(Set<InvoiceProduct> invoiceProducts) {
        this.setInvoiceProducts(invoiceProducts);
        return this;
    }

    public Invoice addInvoiceProduct(InvoiceProduct invoiceProduct) {
        this.invoiceProducts.add(invoiceProduct);
        invoiceProduct.setInvoice(this);
        return this;
    }

    public Invoice removeInvoiceProduct(InvoiceProduct invoiceProduct) {
        this.invoiceProducts.remove(invoiceProduct);
        invoiceProduct.setInvoice(null);
        return this;
    }

    public Set<Inventory> getInventories() {
        return this.inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        if (this.inventories != null) {
            this.inventories.forEach(i -> i.setInvoice(null));
        }
        if (inventories != null) {
            inventories.forEach(i -> i.setInvoice(this));
        }
        this.inventories = inventories;
    }

    public Invoice inventories(Set<Inventory> inventories) {
        this.setInventories(inventories);
        return this;
    }

    public Invoice addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setInvoice(this);
        return this;
    }

    public Invoice removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setInvoice(null);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Invoice order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", iva=" + getIva() +
            ", totalToPay=" + getTotalToPay() +
            "}";
    }
}
