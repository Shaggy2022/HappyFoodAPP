package com.happyfood.service.mapper;

import com.happyfood.domain.Customer;
import com.happyfood.domain.DocumentType;
import com.happyfood.domain.User;
import com.happyfood.service.dto.CustomerDTO;
import com.happyfood.service.dto.DocumentTypeDTO;
import com.happyfood.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "documentTypeDocumentName")
    CustomerDTO toDto(Customer s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("documentTypeDocumentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentName", source = "documentName")
    DocumentTypeDTO toDtoDocumentTypeDocumentName(DocumentType documentType);
}
