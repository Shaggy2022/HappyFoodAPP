package com.happyfood.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.happyfood.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkDayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkDayDTO.class);
        WorkDayDTO workDayDTO1 = new WorkDayDTO();
        workDayDTO1.setId(1L);
        WorkDayDTO workDayDTO2 = new WorkDayDTO();
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
        workDayDTO2.setId(workDayDTO1.getId());
        assertThat(workDayDTO1).isEqualTo(workDayDTO2);
        workDayDTO2.setId(2L);
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
        workDayDTO1.setId(null);
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
    }
}
