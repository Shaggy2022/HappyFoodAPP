package com.happyfood.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.happyfood.IntegrationTest;
import com.happyfood.domain.Horary;
import com.happyfood.repository.HoraryRepository;
import com.happyfood.service.dto.HoraryDTO;
import com.happyfood.service.mapper.HoraryMapper;
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
 * Integration tests for the {@link HoraryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HoraryResourceIT {

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/horaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoraryRepository horaryRepository;

    @Autowired
    private HoraryMapper horaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoraryMockMvc;

    private Horary horary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horary createEntity(EntityManager em) {
        Horary horary = new Horary().startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        return horary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horary createUpdatedEntity(EntityManager em) {
        Horary horary = new Horary().startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        return horary;
    }

    @BeforeEach
    public void initTest() {
        horary = createEntity(em);
    }

    @Test
    @Transactional
    void createHorary() throws Exception {
        int databaseSizeBeforeCreate = horaryRepository.findAll().size();
        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);
        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeCreate + 1);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testHorary.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void createHoraryWithExistingId() throws Exception {
        // Create the Horary with an existing ID
        horary.setId(1L);
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        int databaseSizeBeforeCreate = horaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaryRepository.findAll().size();
        // set the field null
        horary.setStartTime(null);

        // Create the Horary, which fails.
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaryRepository.findAll().size();
        // set the field null
        horary.setEndTime(null);

        // Create the Horary, which fails.
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoraries() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        // Get all the horaryList
        restHoraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horary.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)));
    }

    @Test
    @Transactional
    void getHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        // Get the horary
        restHoraryMockMvc
            .perform(get(ENTITY_API_URL_ID, horary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(horary.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME));
    }

    @Test
    @Transactional
    void getNonExistingHorary() throws Exception {
        // Get the horary
        restHoraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary
        Horary updatedHorary = horaryRepository.findById(horary.getId()).get();
        // Disconnect from session so that the updates on updatedHorary are not directly saved in db
        em.detach(updatedHorary);
        updatedHorary.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        HoraryDTO horaryDTO = horaryMapper.toDto(updatedHorary);

        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHorary.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void putNonExistingHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoraryWithPatch() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary using partial update
        Horary partialUpdatedHorary = new Horary();
        partialUpdatedHorary.setId(horary.getId());

        partialUpdatedHorary.endTime(UPDATED_END_TIME);

        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorary))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testHorary.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void fullUpdateHoraryWithPatch() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary using partial update
        Horary partialUpdatedHorary = new Horary();
        partialUpdatedHorary.setId(horary.getId());

        partialUpdatedHorary.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorary))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHorary.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeDelete = horaryRepository.findAll().size();

        // Delete the horary
        restHoraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, horary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
