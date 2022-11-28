package com.happyfood.service;

import com.happyfood.service.dto.WorkDayDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.happyfood.domain.WorkDay}.
 */
public interface WorkDayService {
    /**
     * Save a workDay.
     *
     * @param workDayDTO the entity to save.
     * @return the persisted entity.
     */
    WorkDayDTO save(WorkDayDTO workDayDTO);

    /**
     * Updates a workDay.
     *
     * @param workDayDTO the entity to update.
     * @return the persisted entity.
     */
    WorkDayDTO update(WorkDayDTO workDayDTO);

    /**
     * Partially updates a workDay.
     *
     * @param workDayDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkDayDTO> partialUpdate(WorkDayDTO workDayDTO);

    /**
     * Get all the workDays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkDayDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workDay.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkDayDTO> findOne(Long id);

    /**
     * Delete the "id" workDay.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
