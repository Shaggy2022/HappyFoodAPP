package com.happyfood.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.happyfood.IntegrationTest;
import com.happyfood.domain.Invoice;
import com.happyfood.domain.InvoiceProduct;
import com.happyfood.domain.Product;
import com.happyfood.repository.InvoiceProductRepository;
import com.happyfood.security.AuthoritiesConstants;
import com.happyfood.service.InvoiceProductService;
import com.happyfood.service.dto.InvoiceProductDTO;
import com.happyfood.service.mapper.InvoiceProductMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvoiceProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = { AuthoritiesConstants.ADMIN, AuthoritiesConstants.MANAGER, AuthoritiesConstants.EMPLOYEE })
class InvoiceProductResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/invoice-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceProductRepository invoiceProductRepository;

    @Mock
    private InvoiceProductRepository invoiceProductRepositoryMock;

    @Autowired
    private InvoiceProductMapper invoiceProductMapper;

    @Mock
    private InvoiceProductService invoiceProductServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceProductMockMvc;

    private InvoiceProduct invoiceProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceProduct createEntity(EntityManager em) {
        InvoiceProduct invoiceProduct = new InvoiceProduct().date(DEFAULT_DATE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        invoiceProduct.setProduct(product);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        invoiceProduct.setInvoice(invoice);
        return invoiceProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceProduct createUpdatedEntity(EntityManager em) {
        InvoiceProduct invoiceProduct = new InvoiceProduct().date(UPDATED_DATE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        invoiceProduct.setProduct(product);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createUpdatedEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        invoiceProduct.setInvoice(invoice);
        return invoiceProduct;
    }

    @BeforeEach
    public void initTest() {
        invoiceProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceProduct() throws Exception {
        int databaseSizeBeforeCreate = invoiceProductRepository.findAll().size();
        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);
        restInvoiceProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceProduct testInvoiceProduct = invoiceProductList.get(invoiceProductList.size() - 1);
        assertThat(testInvoiceProduct.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createInvoiceProductWithExistingId() throws Exception {
        // Create the InvoiceProduct with an existing ID
        invoiceProduct.setId(1L);
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        int databaseSizeBeforeCreate = invoiceProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceProductRepository.findAll().size();
        // set the field null
        invoiceProduct.setDate(null);

        // Create the InvoiceProduct, which fails.
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        restInvoiceProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceProducts() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        // Get all the invoiceProductList
        restInvoiceProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceProductServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceProductServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInvoiceProduct() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        // Get the invoiceProduct
        restInvoiceProductMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceProduct.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceProduct() throws Exception {
        // Get the invoiceProduct
        restInvoiceProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoiceProduct() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();

        // Update the invoiceProduct
        InvoiceProduct updatedInvoiceProduct = invoiceProductRepository.findById(invoiceProduct.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceProduct are not directly saved in db
        em.detach(updatedInvoiceProduct);
        updatedInvoiceProduct.date(UPDATED_DATE);
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(updatedInvoiceProduct);

        restInvoiceProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
        InvoiceProduct testInvoiceProduct = invoiceProductList.get(invoiceProductList.size() - 1);
        assertThat(testInvoiceProduct.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceProductWithPatch() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();

        // Update the invoiceProduct using partial update
        InvoiceProduct partialUpdatedInvoiceProduct = new InvoiceProduct();
        partialUpdatedInvoiceProduct.setId(invoiceProduct.getId());

        partialUpdatedInvoiceProduct.date(UPDATED_DATE);

        restInvoiceProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceProduct))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
        InvoiceProduct testInvoiceProduct = invoiceProductList.get(invoiceProductList.size() - 1);
        assertThat(testInvoiceProduct.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceProductWithPatch() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();

        // Update the invoiceProduct using partial update
        InvoiceProduct partialUpdatedInvoiceProduct = new InvoiceProduct();
        partialUpdatedInvoiceProduct.setId(invoiceProduct.getId());

        partialUpdatedInvoiceProduct.date(UPDATED_DATE);

        restInvoiceProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceProduct))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
        InvoiceProduct testInvoiceProduct = invoiceProductList.get(invoiceProductList.size() - 1);
        assertThat(testInvoiceProduct.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceProductDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceProduct() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProductRepository.findAll().size();
        invoiceProduct.setId(count.incrementAndGet());

        // Create the InvoiceProduct
        InvoiceProductDTO invoiceProductDTO = invoiceProductMapper.toDto(invoiceProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceProduct in the database
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceProduct() throws Exception {
        // Initialize the database
        invoiceProductRepository.saveAndFlush(invoiceProduct);

        int databaseSizeBeforeDelete = invoiceProductRepository.findAll().size();

        // Delete the invoiceProduct
        restInvoiceProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAll();
        assertThat(invoiceProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
