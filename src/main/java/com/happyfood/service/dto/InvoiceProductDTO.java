package com.happyfood.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.happyfood.domain.InvoiceProduct} entity.
 */
public class InvoiceProductDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private ProductDTO product;

    private InvoiceDTO invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceProductDTO)) {
            return false;
        }

        InvoiceProductDTO invoiceProductDTO = (InvoiceProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceProductDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", product=" + getProduct() +
            ", invoice=" + getInvoice() +
            "}";
    }
}
