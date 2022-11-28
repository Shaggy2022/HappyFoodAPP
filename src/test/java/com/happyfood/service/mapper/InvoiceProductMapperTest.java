package com.happyfood.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceProductMapperTest {

    private InvoiceProductMapper invoiceProductMapper;

    @BeforeEach
    public void setUp() {
        invoiceProductMapper = new InvoiceProductMapperImpl();
    }
}
