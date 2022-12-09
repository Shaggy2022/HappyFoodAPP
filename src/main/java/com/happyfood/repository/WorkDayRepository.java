package com.happyfood.repository;

import com.happyfood.domain.DocumentType;
import com.happyfood.domain.WorkDay;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {}
