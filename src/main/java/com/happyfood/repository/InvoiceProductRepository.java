package com.happyfood.repository;

import com.happyfood.domain.InvoiceProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InvoiceProduct entity.
 */
@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    default Optional<InvoiceProduct> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InvoiceProduct> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InvoiceProduct> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct invoiceProduct from InvoiceProduct invoiceProduct left join fetch invoiceProduct.product",
        countQuery = "select count(distinct invoiceProduct) from InvoiceProduct invoiceProduct"
    )
    Page<InvoiceProduct> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct invoiceProduct from InvoiceProduct invoiceProduct left join fetch invoiceProduct.product")
    List<InvoiceProduct> findAllWithToOneRelationships();

    @Query("select invoiceProduct from InvoiceProduct invoiceProduct left join fetch invoiceProduct.product where invoiceProduct.id =:id")
    Optional<InvoiceProduct> findOneWithToOneRelationships(@Param("id") Long id);
}
