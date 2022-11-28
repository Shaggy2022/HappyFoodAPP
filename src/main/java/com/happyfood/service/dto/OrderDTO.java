package com.happyfood.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.happyfood.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String orderRequired;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer amount;

    @NotNull
    private BigDecimal fullOrderValue;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderRequired() {
        return orderRequired;
    }

    public void setOrderRequired(String orderRequired) {
        this.orderRequired = orderRequired;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getFullOrderValue() {
        return fullOrderValue;
    }

    public void setFullOrderValue(BigDecimal fullOrderValue) {
        this.fullOrderValue = fullOrderValue;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", orderRequired='" + getOrderRequired() + "'" +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            ", fullOrderValue=" + getFullOrderValue() +
            ", customer=" + getCustomer() +
            "}";
    }
}
