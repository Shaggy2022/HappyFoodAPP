package com.happyfood.service.impl;

import com.happyfood.domain.Manager;
import com.happyfood.repository.ManagerRepository;
import com.happyfood.service.ManagerService;
import com.happyfood.service.dto.ManagerDTO;
import com.happyfood.service.mapper.ManagerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Manager}.
 */
@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);

    private final ManagerRepository managerRepository;

    private final ManagerMapper managerMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public ManagerDTO save(ManagerDTO managerDTO) {
        log.debug("Request to save Manager : {}", managerDTO);
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }

    @Override
    public ManagerDTO update(ManagerDTO managerDTO) {
        log.debug("Request to save Manager : {}", managerDTO);
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }

    @Override
    public Optional<ManagerDTO> partialUpdate(ManagerDTO managerDTO) {
        log.debug("Request to partially update Manager : {}", managerDTO);

        return managerRepository
            .findById(managerDTO.getId())
            .map(existingManager -> {
                managerMapper.partialUpdate(existingManager, managerDTO);

                return existingManager;
            })
            .map(managerRepository::save)
            .map(managerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Managers");
        return managerRepository.findAll(pageable).map(managerMapper::toDto);
    }

    public Page<ManagerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return managerRepository.findAllWithEagerRelationships(pageable).map(managerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManagerDTO> findOne(Long id) {
        log.debug("Request to get Manager : {}", id);
        return managerRepository.findOneWithEagerRelationships(id).map(managerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Manager : {}", id);
        managerRepository.deleteById(id);
    }
}
