package com.happyfood.service.mapper;

import com.happyfood.domain.Horary;
import com.happyfood.service.dto.HoraryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Horary} and its DTO {@link HoraryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HoraryMapper extends EntityMapper<HoraryDTO, Horary> {}
