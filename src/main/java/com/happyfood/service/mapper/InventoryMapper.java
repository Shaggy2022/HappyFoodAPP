package com.happyfood.service.mapper;

import com.happyfood.domain.Inventory;
import com.happyfood.domain.Invoice;
import com.happyfood.service.dto.InventoryDTO;
import com.happyfood.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceInvoiceNumber")
    InventoryDTO toDto(Inventory s);

    @Named("invoiceInvoiceNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNumber", source = "invoiceNumber")
    InvoiceDTO toDtoInvoiceInvoiceNumber(Invoice invoice);
}
