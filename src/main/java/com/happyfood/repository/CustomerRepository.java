package com.happyfood.repository;

import com.happyfood.domain.Customer;
import com.happyfood.domain.DocumentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    default Optional<Customer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Customer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct customer from Customer customer left join fetch customer.user left join fetch customer.documentType",
        countQuery = "select count(distinct customer) from Customer customer"
    )
    Page<Customer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct customer from Customer customer left join fetch customer.user left join fetch customer.documentType")
    List<Customer> findAllWithToOneRelationships();

    @Query(
        "select customer from Customer customer left join fetch customer.user left join fetch customer.documentType where customer.id =:id"
    )
    Optional<Customer> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<Customer> findByDocumentNumberAndDocumentType(String documentNumber, DocumentType documentType);
    Optional<Customer> findByDocumentNumber(String documentNumber);
}
