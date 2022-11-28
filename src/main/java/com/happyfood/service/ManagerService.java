package com.happyfood.service;

import com.happyfood.service.dto.ManagerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.happyfood.domain.Manager}.
 */
public interface ManagerService {
    /**
     * Save a manager.
     *
     * @param managerDTO the entity to save.
     * @return the persisted entity.
     */
    ManagerDTO save(ManagerDTO managerDTO);

    /**
     * Updates a manager.
     *
     * @param managerDTO the entity to update.
     * @return the persisted entity.
     */
    ManagerDTO update(ManagerDTO managerDTO);

    /**
     * Partially updates a manager.
     *
     * @param managerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManagerDTO> partialUpdate(ManagerDTO managerDTO);

    /**
     * Get all the managers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManagerDTO> findAll(Pageable pageable);

    /**
     * Get all the managers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManagerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" manager.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManagerDTO> findOne(Long id);

    /**
     * Delete the "id" manager.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
