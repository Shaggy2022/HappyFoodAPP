package com.happyfood.service.mapper;

import com.happyfood.domain.Inventory;
import com.happyfood.domain.Product;
import com.happyfood.service.dto.InventoryDTO;
import com.happyfood.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "inventory", source = "inventory", qualifiedByName = "inventoryDescription")
    ProductDTO toDto(Product s);

    @Named("inventoryDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    InventoryDTO toDtoInventoryDescription(Inventory inventory);
}
