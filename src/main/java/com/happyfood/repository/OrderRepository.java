package com.happyfood.repository;

import com.happyfood.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    default Optional<Order> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Order> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Order> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct happyOrder from Order happyOrder left join fetch happyOrder.customer",
        countQuery = "select count(distinct happyOrder) from Order happyOrder"
    )
    Page<Order> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct happyOrder from Order happyOrder left join fetch happyOrder.customer")
    List<Order> findAllWithToOneRelationships();

    @Query("select happyOrder from Order happyOrder left join fetch happyOrder.customer where happyOrder.id =:id")
    Optional<Order> findOneWithToOneRelationships(@Param("id") Long id);
}
