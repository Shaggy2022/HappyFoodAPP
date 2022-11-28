package com.happyfood.service.mapper;

import com.happyfood.domain.Horary;
import com.happyfood.domain.WorkDay;
import com.happyfood.service.dto.HoraryDTO;
import com.happyfood.service.dto.WorkDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkDay} and its DTO {@link WorkDayDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkDayMapper extends EntityMapper<WorkDayDTO, WorkDay> {
    @Mapping(target = "horary", source = "horary", qualifiedByName = "horaryId")
    WorkDayDTO toDto(WorkDay s);

    @Named("horaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HoraryDTO toDtoHoraryId(Horary horary);
}
