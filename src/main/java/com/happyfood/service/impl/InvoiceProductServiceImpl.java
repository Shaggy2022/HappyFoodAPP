package com.happyfood.service.impl;

import com.happyfood.domain.InvoiceProduct;
import com.happyfood.repository.InvoiceProductRepository;
import com.happyfood.service.InvoiceProductService;
import com.happyfood.service.dto.InvoiceProductDTO;
import com.happyfood.service.mapper.InvoiceProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InvoiceProduct}.
 */
@Service
@Transactional
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final Logger log = LoggerFactory.getLogger(InvoiceProductServiceImpl.class);

    private final InvoiceProductRepository invoiceProductRepository;

    private final InvoiceProductMapper invoiceProductMapper;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, InvoiceProductMapper invoiceProductMapper) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceProductMapper = invoiceProductMapper;
    }

    @Override
    public InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO) {
        log.debug("Request to save InvoiceProduct : {}", invoiceProductDTO);
        InvoiceProduct invoiceProduct = invoiceProductMapper.toEntity(invoiceProductDTO);
        invoiceProduct = invoiceProductRepository.save(invoiceProduct);
        return invoiceProductMapper.toDto(invoiceProduct);
    }

    @Override
    public InvoiceProductDTO update(InvoiceProductDTO invoiceProductDTO) {
        log.debug("Request to save InvoiceProduct : {}", invoiceProductDTO);
        InvoiceProduct invoiceProduct = invoiceProductMapper.toEntity(invoiceProductDTO);
        invoiceProduct = invoiceProductRepository.save(invoiceProduct);
        return invoiceProductMapper.toDto(invoiceProduct);
    }

    @Override
    public Optional<InvoiceProductDTO> partialUpdate(InvoiceProductDTO invoiceProductDTO) {
        log.debug("Request to partially update InvoiceProduct : {}", invoiceProductDTO);

        return invoiceProductRepository
            .findById(invoiceProductDTO.getId())
            .map(existingInvoiceProduct -> {
                invoiceProductMapper.partialUpdate(existingInvoiceProduct, invoiceProductDTO);

                return existingInvoiceProduct;
            })
            .map(invoiceProductRepository::save)
            .map(invoiceProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceProducts");
        return invoiceProductRepository.findAll(pageable).map(invoiceProductMapper::toDto);
    }

    public Page<InvoiceProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceProductRepository.findAllWithEagerRelationships(pageable).map(invoiceProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceProductDTO> findOne(Long id) {
        log.debug("Request to get InvoiceProduct : {}", id);
        return invoiceProductRepository.findOneWithEagerRelationships(id).map(invoiceProductMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceProduct : {}", id);
        invoiceProductRepository.deleteById(id);
    }
}
