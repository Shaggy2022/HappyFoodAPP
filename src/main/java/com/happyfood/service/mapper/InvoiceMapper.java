package com.happyfood.service.mapper;

import com.happyfood.domain.Invoice;
import com.happyfood.domain.Order;
import com.happyfood.service.dto.InvoiceDTO;
import com.happyfood.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderOrderRequired")
    InvoiceDTO toDto(Invoice s);

    @Named("orderOrderRequired")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderRequired", source = "orderRequired")
    OrderDTO toDtoOrderOrderRequired(Order order);
}
