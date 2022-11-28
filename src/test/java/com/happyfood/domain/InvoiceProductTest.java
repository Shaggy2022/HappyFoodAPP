package com.happyfood.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.happyfood.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceProduct.class);
        InvoiceProduct invoiceProduct1 = new InvoiceProduct();
        invoiceProduct1.setId(1L);
        InvoiceProduct invoiceProduct2 = new InvoiceProduct();
        invoiceProduct2.setId(invoiceProduct1.getId());
        assertThat(invoiceProduct1).isEqualTo(invoiceProduct2);
        invoiceProduct2.setId(2L);
        assertThat(invoiceProduct1).isNotEqualTo(invoiceProduct2);
        invoiceProduct1.setId(null);
        assertThat(invoiceProduct1).isNotEqualTo(invoiceProduct2);
    }
}
