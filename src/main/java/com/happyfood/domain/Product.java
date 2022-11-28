package com.happyfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "serial", length = 255, nullable = false, unique = true)
    private String serial;

    @NotNull
    @Size(max = 200)
    @Column(name = "required_product", length = 200, nullable = false)
    private String requiredProduct;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product", "invoice" }, allowSetters = true)
    private Set<InvoiceProduct> invoiceProducts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "invoice" }, allowSetters = true)
    private Inventory inventory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return this.serial;
    }

    public Product serial(String serial) {
        this.setSerial(serial);
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getRequiredProduct() {
        return this.requiredProduct;
    }

    public Product requiredProduct(String requiredProduct) {
        this.setRequiredProduct(requiredProduct);
        return this;
    }

    public void setRequiredProduct(String requiredProduct) {
        this.requiredProduct = requiredProduct;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<InvoiceProduct> getInvoiceProducts() {
        return this.invoiceProducts;
    }

    public void setInvoiceProducts(Set<InvoiceProduct> invoiceProducts) {
        if (this.invoiceProducts != null) {
            this.invoiceProducts.forEach(i -> i.setProduct(null));
        }
        if (invoiceProducts != null) {
            invoiceProducts.forEach(i -> i.setProduct(this));
        }
        this.invoiceProducts = invoiceProducts;
    }

    public Product invoiceProducts(Set<InvoiceProduct> invoiceProducts) {
        this.setInvoiceProducts(invoiceProducts);
        return this;
    }

    public Product addInvoiceProduct(InvoiceProduct invoiceProduct) {
        this.invoiceProducts.add(invoiceProduct);
        invoiceProduct.setProduct(this);
        return this;
    }

    public Product removeInvoiceProduct(InvoiceProduct invoiceProduct) {
        this.invoiceProducts.remove(invoiceProduct);
        invoiceProduct.setProduct(null);
        return this;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Product inventory(Inventory inventory) {
        this.setInventory(inventory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", serial='" + getSerial() + "'" +
            ", requiredProduct='" + getRequiredProduct() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
