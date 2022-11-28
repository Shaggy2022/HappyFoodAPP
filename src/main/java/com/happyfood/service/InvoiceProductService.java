package com.happyfood.service;

import com.happyfood.service.dto.InvoiceProductDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.happyfood.domain.InvoiceProduct}.
 */
public interface InvoiceProductService {
    /**
     * Save a invoiceProduct.
     *
     * @param invoiceProductDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO);

    /**
     * Updates a invoiceProduct.
     *
     * @param invoiceProductDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceProductDTO update(InvoiceProductDTO invoiceProductDTO);

    /**
     * Partially updates a invoiceProduct.
     *
     * @param invoiceProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceProductDTO> partialUpdate(InvoiceProductDTO invoiceProductDTO);

    /**
     * Get all the invoiceProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceProductDTO> findAll(Pageable pageable);

    /**
     * Get all the invoiceProducts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceProductDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" invoiceProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceProductDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
