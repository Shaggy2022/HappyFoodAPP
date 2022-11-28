package com.happyfood.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.happyfood.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkDay.class);
        WorkDay workDay1 = new WorkDay();
        workDay1.setId(1L);
        WorkDay workDay2 = new WorkDay();
        workDay2.setId(workDay1.getId());
        assertThat(workDay1).isEqualTo(workDay2);
        workDay2.setId(2L);
        assertThat(workDay1).isNotEqualTo(workDay2);
        workDay1.setId(null);
        assertThat(workDay1).isNotEqualTo(workDay2);
    }
}
