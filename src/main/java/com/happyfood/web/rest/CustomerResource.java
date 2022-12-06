package com.happyfood.web.rest;

import com.happyfood.domain.DocumentType;
import com.happyfood.repository.CustomerRepository;
import com.happyfood.repository.DocumentTypeRepository;
import com.happyfood.security.AuthoritiesConstants;
import com.happyfood.service.CustomerService;
import com.happyfood.service.dto.CustomerDTO;
import com.happyfood.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.print.Doc;
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
 * REST controller for managing {@link com.happyfood.domain.Customer}.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final DocumentTypeRepository documentTypeRepository;

    private final CustomerRepository customerRepository;

    public CustomerResource(
        CustomerService customerService,
        DocumentTypeRepository documentTypeRepository,
        CustomerRepository customerRepository
    ) {
        this.customerService = customerService;
        this.documentTypeRepository = documentTypeRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDTO, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customers")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        Optional<DocumentType> documentTypeOptional = documentTypeRepository.findById(customerDTO.getDocumentType().getId());
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        } else if (documentTypeOptional.isEmpty()) {
            throw new BadRequestAlertException("The document type doesnot Exist", ENTITY_NAME, "DocumentTypeDoesNotExists");
        } else if (
            customerRepository.findByDocumentNumberAndDocumentType(customerDTO.getDocumentNumber(), documentTypeOptional.get()).isPresent()
        ) {
            throw new BadRequestAlertException(
                "A customer with the same type and document number already exists.",
                ENTITY_NAME,
                "DocumentTypeAndDocumentNumberExists"
            );
        } else if (customerRepository.findByDocumentNumber(customerDTO.getDocumentNumber()).isPresent()) {
            throw new BadRequestAlertException(
                "There is already a customer with the same document number.",
                ENTITY_NAME,
                "DocumentNumberExists"
            );
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity
            .created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customers/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Customer : {}, {}", id, customerDTO);
        Optional<DocumentType> documentTypeOptional = documentTypeRepository.findById(customerDTO.getDocumentType().getId());
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        } else if (documentTypeOptional.isEmpty()) {
            throw new BadRequestAlertException("The document type doesnot Exist", ENTITY_NAME, "DocumentTypeDoesNotExists");
        } else if (
            customerRepository.findByDocumentNumberAndDocumentType(customerDTO.getDocumentNumber(), documentTypeOptional.get()).isPresent()
        ) {
            throw new BadRequestAlertException(
                "A customer with the same type and document number already exists.",
                ENTITY_NAME,
                "DocumentTypeAndDocumentNumberExists"
            );
        } else if (customerRepository.findByDocumentNumber(customerDTO.getDocumentNumber()).isPresent()) {
            throw new BadRequestAlertException(
                "There is already a customer with the same document number.",
                ENTITY_NAME,
                "DocumentNumberExists"
            );
        }

        CustomerDTO result = customerService.update(customerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\")"
    )
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customer partially : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDTO> result = customerService.partialUpdate(customerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.CUSTOMER +
        "\")"
    )
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Customers");
        Page<CustomerDTO> page;
        if (eagerload) {
            page = customerService.findAllWithEagerRelationships(pageable);
        } else {
            page = customerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customers/{id}")
    @PreAuthorize(
        "hasAuthority(\"" +
        AuthoritiesConstants.ADMIN +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.MANAGER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.USER +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.EMPLOYEE +
        "\") or hasAuthority(\"" +
        AuthoritiesConstants.CUSTOMER +
        "\")"
    )
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
