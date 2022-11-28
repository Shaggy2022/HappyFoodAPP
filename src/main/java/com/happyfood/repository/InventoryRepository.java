package com.happyfood.repository;

import com.happyfood.domain.Inventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inventory entity.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    default Optional<Inventory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inventory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inventory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inventory from Inventory inventory left join fetch inventory.invoice",
        countQuery = "select count(distinct inventory) from Inventory inventory"
    )
    Page<Inventory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct inventory from Inventory inventory left join fetch inventory.invoice")
    List<Inventory> findAllWithToOneRelationships();

    @Query("select inventory from Inventory inventory left join fetch inventory.invoice where inventory.id =:id")
    Optional<Inventory> findOneWithToOneRelationships(@Param("id") Long id);
}
