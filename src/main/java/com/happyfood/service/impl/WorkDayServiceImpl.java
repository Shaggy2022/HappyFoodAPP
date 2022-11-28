package com.happyfood.service.impl;

import com.happyfood.domain.WorkDay;
import com.happyfood.repository.WorkDayRepository;
import com.happyfood.service.WorkDayService;
import com.happyfood.service.dto.WorkDayDTO;
import com.happyfood.service.mapper.WorkDayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkDay}.
 */
@Service
@Transactional
public class WorkDayServiceImpl implements WorkDayService {

    private final Logger log = LoggerFactory.getLogger(WorkDayServiceImpl.class);

    private final WorkDayRepository workDayRepository;

    private final WorkDayMapper workDayMapper;

    public WorkDayServiceImpl(WorkDayRepository workDayRepository, WorkDayMapper workDayMapper) {
        this.workDayRepository = workDayRepository;
        this.workDayMapper = workDayMapper;
    }

    @Override
    public WorkDayDTO save(WorkDayDTO workDayDTO) {
        log.debug("Request to save WorkDay : {}", workDayDTO);
        WorkDay workDay = workDayMapper.toEntity(workDayDTO);
        workDay = workDayRepository.save(workDay);
        return workDayMapper.toDto(workDay);
    }

    @Override
    public WorkDayDTO update(WorkDayDTO workDayDTO) {
        log.debug("Request to save WorkDay : {}", workDayDTO);
        WorkDay workDay = workDayMapper.toEntity(workDayDTO);
        workDay = workDayRepository.save(workDay);
        return workDayMapper.toDto(workDay);
    }

    @Override
    public Optional<WorkDayDTO> partialUpdate(WorkDayDTO workDayDTO) {
        log.debug("Request to partially update WorkDay : {}", workDayDTO);

        return workDayRepository
            .findById(workDayDTO.getId())
            .map(existingWorkDay -> {
                workDayMapper.partialUpdate(existingWorkDay, workDayDTO);

                return existingWorkDay;
            })
            .map(workDayRepository::save)
            .map(workDayMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkDayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkDays");
        return workDayRepository.findAll(pageable).map(workDayMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkDayDTO> findOne(Long id) {
        log.debug("Request to get WorkDay : {}", id);
        return workDayRepository.findById(id).map(workDayMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkDay : {}", id);
        workDayRepository.deleteById(id);
    }
}
