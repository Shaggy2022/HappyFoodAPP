package com.happyfood.repository;

import com.happyfood.domain.WorkDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {}
