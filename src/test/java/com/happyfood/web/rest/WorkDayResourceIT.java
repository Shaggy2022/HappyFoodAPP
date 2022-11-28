package com.happyfood.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.happyfood.IntegrationTest;
import com.happyfood.domain.Horary;
import com.happyfood.domain.WorkDay;
import com.happyfood.domain.enumeration.State;
import com.happyfood.repository.WorkDayRepository;
import com.happyfood.service.dto.WorkDayDTO;
import com.happyfood.service.mapper.WorkDayMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkDayResourceIT {

    private static final String DEFAULT_DAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.ACTIVE;
    private static final State UPDATED_STATE = State.INACTIVE;

    private static final String ENTITY_API_URL = "/api/work-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkDayRepository workDayRepository;

    @Autowired
    private WorkDayMapper workDayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkDayMockMvc;

    private WorkDay workDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkDay createEntity(EntityManager em) {
        WorkDay workDay = new WorkDay().dayName(DEFAULT_DAY_NAME).month(DEFAULT_MONTH).state(DEFAULT_STATE);
        // Add required entity
        Horary horary;
        if (TestUtil.findAll(em, Horary.class).isEmpty()) {
            horary = HoraryResourceIT.createEntity(em);
            em.persist(horary);
            em.flush();
        } else {
            horary = TestUtil.findAll(em, Horary.class).get(0);
        }
        workDay.setHorary(horary);
        return workDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkDay createUpdatedEntity(EntityManager em) {
        WorkDay workDay = new WorkDay().dayName(UPDATED_DAY_NAME).month(UPDATED_MONTH).state(UPDATED_STATE);
        // Add required entity
        Horary horary;
        if (TestUtil.findAll(em, Horary.class).isEmpty()) {
            horary = HoraryResourceIT.createUpdatedEntity(em);
            em.persist(horary);
            em.flush();
        } else {
            horary = TestUtil.findAll(em, Horary.class).get(0);
        }
        workDay.setHorary(horary);
        return workDay;
    }

    @BeforeEach
    public void initTest() {
        workDay = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkDay() throws Exception {
        int databaseSizeBeforeCreate = workDayRepository.findAll().size();
        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);
        restWorkDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeCreate + 1);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.getDayName()).isEqualTo(DEFAULT_DAY_NAME);
        assertThat(testWorkDay.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testWorkDay.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createWorkDayWithExistingId() throws Exception {
        // Create the WorkDay with an existing ID
        workDay.setId(1L);
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        int databaseSizeBeforeCreate = workDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setDayName(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setMonth(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setState(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkDays() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        // Get all the workDayList
        restWorkDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayName").value(hasItem(DEFAULT_DAY_NAME)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        // Get the workDay
        restWorkDayMockMvc
            .perform(get(ENTITY_API_URL_ID, workDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workDay.getId().intValue()))
            .andExpect(jsonPath("$.dayName").value(DEFAULT_DAY_NAME))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkDay() throws Exception {
        // Get the workDay
        restWorkDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();

        // Update the workDay
        WorkDay updatedWorkDay = workDayRepository.findById(workDay.getId()).get();
        // Disconnect from session so that the updates on updatedWorkDay are not directly saved in db
        em.detach(updatedWorkDay);
        updatedWorkDay.dayName(UPDATED_DAY_NAME).month(UPDATED_MONTH).state(UPDATED_STATE);
        WorkDayDTO workDayDTO = workDayMapper.toDto(updatedWorkDay);

        restWorkDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.getDayName()).isEqualTo(UPDATED_DAY_NAME);
        assertThat(testWorkDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testWorkDay.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkDayWithPatch() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();

        // Update the workDay using partial update
        WorkDay partialUpdatedWorkDay = new WorkDay();
        partialUpdatedWorkDay.setId(workDay.getId());

        partialUpdatedWorkDay.dayName(UPDATED_DAY_NAME).month(UPDATED_MONTH);

        restWorkDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkDay))
            )
            .andExpect(status().isOk());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.getDayName()).isEqualTo(UPDATED_DAY_NAME);
        assertThat(testWorkDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testWorkDay.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void fullUpdateWorkDayWithPatch() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();

        // Update the workDay using partial update
        WorkDay partialUpdatedWorkDay = new WorkDay();
        partialUpdatedWorkDay.setId(workDay.getId());

        partialUpdatedWorkDay.dayName(UPDATED_DAY_NAME).month(UPDATED_MONTH).state(UPDATED_STATE);

        restWorkDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkDay))
            )
            .andExpect(status().isOk());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.getDayName()).isEqualTo(UPDATED_DAY_NAME);
        assertThat(testWorkDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testWorkDay.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workDayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();
        workDay.setId(count.incrementAndGet());

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workDayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeDelete = workDayRepository.findAll().size();

        // Delete the workDay
        restWorkDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, workDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
