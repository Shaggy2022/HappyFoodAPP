package com.happyfood.service.mapper;

import com.happyfood.domain.Invoice;
import com.happyfood.domain.InvoiceProduct;
import com.happyfood.domain.Product;
import com.happyfood.service.dto.InvoiceDTO;
import com.happyfood.service.dto.InvoiceProductDTO;
import com.happyfood.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceProduct} and its DTO {@link InvoiceProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceProductMapper extends EntityMapper<InvoiceProductDTO, InvoiceProduct> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productSerial")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceId")
    InvoiceProductDTO toDto(InvoiceProduct s);

    @Named("productSerial")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serial", source = "serial")
    ProductDTO toDtoProductSerial(Product product);

    @Named("invoiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoiceDTO toDtoInvoiceId(Invoice invoice);
}
