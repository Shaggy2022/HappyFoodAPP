package com.happyfood.web.rest;

import com.happyfood.repository.WorkDayRepository;
import com.happyfood.security.AuthoritiesConstants;
import com.happyfood.service.WorkDayService;
import com.happyfood.service.dto.WorkDayDTO;
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
 * REST controller for managing {@link com.happyfood.domain.WorkDay}.
 */
@RestController
@RequestMapping("/api")
public class WorkDayResource {

    private final Logger log = LoggerFactory.getLogger(WorkDayResource.class);

    private static final String ENTITY_NAME = "workDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkDayService workDayService;

    private final WorkDayRepository workDayRepository;

    public WorkDayResource(WorkDayService workDayService, WorkDayRepository workDayRepository) {
        this.workDayService = workDayService;
        this.workDayRepository = workDayRepository;
    }

    /**
     * {@code POST  /work-days} : Create a new workDay.
     *
     * @param workDayDTO the workDayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workDayDTO, or with status {@code 400 (Bad Request)} if the workDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-days")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<WorkDayDTO> createWorkDay(@Valid @RequestBody WorkDayDTO workDayDTO) throws URISyntaxException {
        log.debug("REST request to save WorkDay : {}", workDayDTO);
        if (workDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new workDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkDayDTO result = workDayService.save(workDayDTO);
        return ResponseEntity
            .created(new URI("/api/work-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-days/:id} : Updates an existing workDay.
     *
     * @param id the id of the workDayDTO to save.
     * @param workDayDTO the workDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workDayDTO,
     * or with status {@code 400 (Bad Request)} if the workDayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-days/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<WorkDayDTO> updateWorkDay(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkDayDTO workDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkDay : {}, {}", id, workDayDTO);
        if (workDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkDayDTO result = workDayService.update(workDayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-days/:id} : Partial updates given fields of an existing workDay, field will ignore if it is null
     *
     * @param id the id of the workDayDTO to save.
     * @param workDayDTO the workDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workDayDTO,
     * or with status {@code 400 (Bad Request)} if the workDayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workDayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-days/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<WorkDayDTO> partialUpdateWorkDay(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkDayDTO workDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkDay partially : {}, {}", id, workDayDTO);
        if (workDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkDayDTO> result = workDayService.partialUpdate(workDayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workDayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-days} : get all the workDays.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workDays in body.
     */
    @GetMapping("/work-days")
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
    public ResponseEntity<List<WorkDayDTO>> getAllWorkDays(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WorkDays");
        Page<WorkDayDTO> page = workDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-days/:id} : get the "id" workDay.
     *
     * @param id the id of the workDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-days/{id}")
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
    public ResponseEntity<WorkDayDTO> getWorkDay(@PathVariable Long id) {
        log.debug("REST request to get WorkDay : {}", id);
        Optional<WorkDayDTO> workDayDTO = workDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workDayDTO);
    }

    /**
     * {@code DELETE  /work-days/:id} : delete the "id" workDay.
     *
     * @param id the id of the workDayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-days/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<Void> deleteWorkDay(@PathVariable Long id) {
        log.debug("REST request to delete WorkDay : {}", id);
        workDayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
