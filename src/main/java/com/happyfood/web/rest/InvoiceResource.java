package com.happyfood.web.rest;

import com.happyfood.domain.DocumentType;
import com.happyfood.domain.Invoice;
import com.happyfood.repository.InvoiceRepository;
import com.happyfood.security.AuthoritiesConstants;
import com.happyfood.service.InvoiceService;
import com.happyfood.service.dto.InvoiceDTO;
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
 * REST controller for managing {@link com.happyfood.domain.Invoice}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public InvoiceResource(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * {@code POST  /invoices} : Create a new invoice.
     *
     * @param invoiceDTO the invoiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceDTO, or with status {@code 400 (Bad Request)} if the invoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoices")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoiceDTO);
        if (invoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoice cannot already have an ID", ENTITY_NAME, "idexists");
        } else if (invoiceRepository.findByInvoiceNumber(invoiceDTO.getInvoiceNumber()).isPresent()) {
            throw new BadRequestAlertException(
                "There is already an invoice with the same invoice number.",
                ENTITY_NAME,
                "InvoiceNumberExists"
            );
        }
        InvoiceDTO result = invoiceService.save(invoiceDTO);
        return ResponseEntity
            .created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoices/:id} : Updates an existing invoice.
     *
     * @param id the id of the invoiceDTO to save.
     * @param invoiceDTO the invoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoices/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<InvoiceDTO> updateInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceDTO invoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}, {}", id, invoiceDTO);
        if (invoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        InvoiceDTO result = invoiceService.update(invoiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoices/:id} : Partial updates given fields of an existing invoice, field will ignore if it is null
     *
     * @param id the id of the invoiceDTO to save.
     * @param invoiceDTO the invoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<InvoiceDTO> partialUpdateInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceDTO invoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Invoice partially : {}, {}", id, invoiceDTO);
        if (invoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceDTO> result = invoiceService.partialUpdate(invoiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoices} : get all the invoices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoices in body.
     */
    @GetMapping("/invoices")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.CUSTOMER +
        "\")"
    )
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Invoices");
        Page<InvoiceDTO> page;
        if (eagerload) {
            page = invoiceService.findAllWithEagerRelationships(pageable);
        } else {
            page = invoiceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoices/:id} : get the "id" invoice.
     *
     * @param id the id of the invoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoices/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.CUSTOMER +
        "\")"
    )
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDTO);
    }

    /**
     * {@code DELETE  /invoices/:id} : delete the "id" invoice.
     *
     * @param id the id of the invoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoices/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
