package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.Contract;
import net.juancarlosfernandez.jhipster.repository.ContractRepository;
import net.juancarlosfernandez.jhipster.service.dto.ContractDTO;
import net.juancarlosfernandez.jhipster.service.mapper.ContractMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.juancarlosfernandez.jhipster.domain.enumeration.Status;
/**
 * Test class for the ContractResource REST controller.
 *
 * @see ContractResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class ContractResourceIntTest {

    private static final String DEFAULT_CONTRACT_NAME = "AAAAA";
    private static final String UPDATED_CONTRACT_NAME = "BBBBB";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final byte[] DEFAULT_DOCUMENT_SIGNED = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT_SIGNED = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DOCUMENT_SIGNED_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_SIGNED_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_SIGNED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SIGNED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SIGNED_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_SIGNED_DATE);

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ContractMapper contractMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractResource contractResource = new ContractResource();
        ReflectionTestUtils.setField(contractResource, "contractRepository", contractRepository);
        ReflectionTestUtils.setField(contractResource, "contractMapper", contractMapper);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
                .contractName(DEFAULT_CONTRACT_NAME)
                .document(DEFAULT_DOCUMENT)
                .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE)
                .status(DEFAULT_STATUS)
                .documentSigned(DEFAULT_DOCUMENT_SIGNED)
                .documentSignedContentType(DEFAULT_DOCUMENT_SIGNED_CONTENT_TYPE)
                .creationDate(DEFAULT_CREATION_DATE)
                .signedDate(DEFAULT_SIGNED_DATE);
        return contract;
    }

    @Before
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract
        ContractDTO contractDTO = contractMapper.contractToContractDTO(contract);

        restContractMockMvc.perform(post("/api/contracts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
                .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contracts.get(contracts.size() - 1);
        assertThat(testContract.getContractName()).isEqualTo(DEFAULT_CONTRACT_NAME);
        assertThat(testContract.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testContract.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
        assertThat(testContract.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testContract.getDocumentSigned()).isEqualTo(DEFAULT_DOCUMENT_SIGNED);
        assertThat(testContract.getDocumentSignedContentType()).isEqualTo(DEFAULT_DOCUMENT_SIGNED_CONTENT_TYPE);
        // Creation date now is generated automatically by the system.
        //assertThat(testContract.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testContract.getSignedDate()).isEqualTo(DEFAULT_SIGNED_DATE);
    }

    @Test
    @Transactional
    public void checkContractNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setContractName(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.contractToContractDTO(contract);

        restContractMockMvc.perform(post("/api/contracts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
                .andExpect(status().isBadRequest());

        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contracts
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
                .andExpect(jsonPath("$.[*].contractName").value(hasItem(DEFAULT_CONTRACT_NAME.toString())))
                .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].documentSignedContentType").value(hasItem(DEFAULT_DOCUMENT_SIGNED_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].documentSigned").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT_SIGNED))))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].signedDate").value(hasItem(DEFAULT_SIGNED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.contractName").value(DEFAULT_CONTRACT_NAME.toString()))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.documentSignedContentType").value(DEFAULT_DOCUMENT_SIGNED_CONTENT_TYPE))
            .andExpect(jsonPath("$.documentSigned").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT_SIGNED)))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.signedDate").value(DEFAULT_SIGNED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findOne(contract.getId());
        updatedContract
                .contractName(UPDATED_CONTRACT_NAME)
                .document(UPDATED_DOCUMENT)
                .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
                .status(UPDATED_STATUS)
                .documentSigned(UPDATED_DOCUMENT_SIGNED)
                .documentSignedContentType(UPDATED_DOCUMENT_SIGNED_CONTENT_TYPE)
                .creationDate(UPDATED_CREATION_DATE)
                .signedDate(UPDATED_SIGNED_DATE);
        ContractDTO contractDTO = contractMapper.contractToContractDTO(updatedContract);

        restContractMockMvc.perform(put("/api/contracts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
                .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contracts.get(contracts.size() - 1);
        assertThat(testContract.getContractName()).isEqualTo(UPDATED_CONTRACT_NAME);
        assertThat(testContract.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testContract.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
        assertThat(testContract.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testContract.getDocumentSigned()).isEqualTo(UPDATED_DOCUMENT_SIGNED);
        assertThat(testContract.getDocumentSignedContentType()).isEqualTo(UPDATED_DOCUMENT_SIGNED_CONTENT_TYPE);
        assertThat(testContract.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testContract.getSignedDate()).isEqualTo(UPDATED_SIGNED_DATE);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);
        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Get the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
