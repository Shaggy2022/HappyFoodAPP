package com.happyfood.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkDayMapperTest {

    private WorkDayMapper workDayMapper;

    @BeforeEach
    public void setUp() {
        workDayMapper = new WorkDayMapperImpl();
    }
}
