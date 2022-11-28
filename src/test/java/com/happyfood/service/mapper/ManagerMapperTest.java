package com.happyfood.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManagerMapperTest {

    private ManagerMapper managerMapper;

    @BeforeEach
    public void setUp() {
        managerMapper = new ManagerMapperImpl();
    }
}
