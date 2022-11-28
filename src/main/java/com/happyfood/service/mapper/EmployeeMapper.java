package com.happyfood.service.mapper;

import com.happyfood.domain.Employee;
import com.happyfood.domain.Manager;
import com.happyfood.domain.User;
import com.happyfood.domain.WorkDay;
import com.happyfood.service.dto.EmployeeDTO;
import com.happyfood.service.dto.ManagerDTO;
import com.happyfood.service.dto.UserDTO;
import com.happyfood.service.dto.WorkDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "workDay", source = "workDay", qualifiedByName = "workDayDayName")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "managerFirstName")
    EmployeeDTO toDto(Employee s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("workDayDayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dayName", source = "dayName")
    WorkDayDTO toDtoWorkDayDayName(WorkDay workDay);

    @Named("managerFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    ManagerDTO toDtoManagerFirstName(Manager manager);
}
