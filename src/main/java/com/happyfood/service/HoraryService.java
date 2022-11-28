package com.happyfood.service;

import com.happyfood.service.dto.HoraryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.happyfood.domain.Horary}.
 */
public interface HoraryService {
    /**
     * Save a horary.
     *
     * @param horaryDTO the entity to save.
     * @return the persisted entity.
     */
    HoraryDTO save(HoraryDTO horaryDTO);

    /**
     * Updates a horary.
     *
     * @param horaryDTO the entity to update.
     * @return the persisted entity.
     */
    HoraryDTO update(HoraryDTO horaryDTO);

    /**
     * Partially updates a horary.
     *
     * @param horaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HoraryDTO> partialUpdate(HoraryDTO horaryDTO);

    /**
     * Get all the horaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HoraryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" horary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HoraryDTO> findOne(Long id);

    /**
     * Delete the "id" horary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
