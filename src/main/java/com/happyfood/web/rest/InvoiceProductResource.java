package com.happyfood.web.rest;

import com.happyfood.repository.InvoiceProductRepository;
import com.happyfood.security.AuthoritiesConstants;
import com.happyfood.service.InvoiceProductService;
import com.happyfood.service.dto.InvoiceProductDTO;
import com.happyfood.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.happyfood.domain.InvoiceProduct}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceProductResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceProductResource.class);

    private static final String ENTITY_NAME = "invoiceProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceProductService invoiceProductService;

    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductResource(InvoiceProductService invoiceProductService, InvoiceProductRepository invoiceProductRepository) {
        this.invoiceProductService = invoiceProductService;
        this.invoiceProductRepository = invoiceProductRepository;
    }

    /**
     * {@code POST  /invoice-products} : Create a new invoiceProduct.
     *
     * @param invoiceProductDTO the invoiceProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceProductDTO, or with status {@code 400 (Bad Request)} if the invoiceProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<InvoiceProductDTO> createInvoiceProduct(@Valid @RequestBody InvoiceProductDTO invoiceProductDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvoiceProduct : {}", invoiceProductDTO);
        if (invoiceProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceProductDTO result = invoiceProductService.save(invoiceProductDTO);
        return ResponseEntity
            .created(new URI("/api/invoice-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-products/:id} : Updates an existing invoiceProduct.
     *
     * @param id the id of the invoiceProductDTO to save.
     * @param invoiceProductDTO the invoiceProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceProductDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<InvoiceProductDTO> updateInvoiceProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceProductDTO invoiceProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceProduct : {}, {}", id, invoiceProductDTO);
        if (invoiceProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceProductDTO result = invoiceProductService.update(invoiceProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-products/:id} : Partial updates given fields of an existing invoiceProduct, field will ignore if it is null
     *
     * @param id the id of the invoiceProductDTO to save.
     * @param invoiceProductDTO the invoiceProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceProductDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<InvoiceProductDTO> partialUpdateInvoiceProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceProductDTO invoiceProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceProduct partially : {}, {}", id, invoiceProductDTO);
        if (invoiceProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceProductDTO> result = invoiceProductService.partialUpdate(invoiceProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-products} : get all the invoiceProducts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceProducts in body.
     */
    @GetMapping("/invoice-products")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\")"
    )
    public ResponseEntity<List<InvoiceProductDTO>> getAllInvoiceProducts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of InvoiceProducts");
        Page<InvoiceProductDTO> page;
        if (eagerload) {
            page = invoiceProductService.findAllWithEagerRelationships(pageable);
        } else {
            page = invoiceProductService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-products/:id} : get the "id" invoiceProduct.
     *
     * @param id the id of the invoiceProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-products/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\")"
    )
    public ResponseEntity<InvoiceProductDTO> getInvoiceProduct(@PathVariable Long id) {
        log.debug("REST request to get InvoiceProduct : {}", id);
        Optional<InvoiceProductDTO> invoiceProductDTO = invoiceProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceProductDTO);
    }

    /**
     * {@code DELETE  /invoice-products/:id} : delete the "id" invoiceProduct.
     *
     * @param id the id of the invoiceProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteInvoiceProduct(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceProduct : {}", id);
        invoiceProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
