package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.SignRequest;
import net.juancarlosfernandez.jhipster.repository.SignRequestRepository;
import net.juancarlosfernandez.jhipster.service.dto.SignRequestDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignRequestMapper;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SignRequestResource REST controller.
 *
 * @see SignRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class SignRequestResourceIntTest {

    private static final String DEFAULT_BODY_EMAIL = "AAAAA";
    private static final String UPDATED_BODY_EMAIL = "BBBBB";

    private static final String DEFAULT_SUBJECT_EMAIL = "AAAAA";
    private static final String UPDATED_SUBJECT_EMAIL = "BBBBB";

    private static final String DEFAULT_SIGNATURIT_ID = "AAAAA";
    private static final String UPDATED_SIGNATURIT_ID = "BBBBB";

    private static final String DEFAULT_SIGNATURIT_DOCUMENT_ID = "AAAAA";
    private static final String UPDATED_SIGNATURIT_DOCUMENT_ID = "BBBBB";

    @Inject
    private SignRequestRepository signRequestRepository;

    @Inject
    private SignRequestMapper signRequestMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSignRequestMockMvc;

    private SignRequest signRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignRequestResource signRequestResource = new SignRequestResource();
        ReflectionTestUtils.setField(signRequestResource, "signRequestRepository", signRequestRepository);
        ReflectionTestUtils.setField(signRequestResource, "signRequestMapper", signRequestMapper);
        this.restSignRequestMockMvc = MockMvcBuilders.standaloneSetup(signRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignRequest createEntity(EntityManager em) {
        SignRequest signRequest = new SignRequest()
                .bodyEmail(DEFAULT_BODY_EMAIL)
                .subjectEmail(DEFAULT_SUBJECT_EMAIL)
                .signaturitId(DEFAULT_SIGNATURIT_ID)
                .signaturitDocumentId(DEFAULT_SIGNATURIT_DOCUMENT_ID);
        return signRequest;
    }

    @Before
    public void initTest() {
        signRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignRequest() throws Exception {
        int databaseSizeBeforeCreate = signRequestRepository.findAll().size();

        // Create the SignRequest
        SignRequestDTO signRequestDTO = signRequestMapper.signRequestToSignRequestDTO(signRequest);

        restSignRequestMockMvc.perform(post("/api/sign-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signRequestDTO)))
                .andExpect(status().isCreated());

        // Validate the SignRequest in the database
        List<SignRequest> signRequests = signRequestRepository.findAll();
        assertThat(signRequests).hasSize(databaseSizeBeforeCreate + 1);
        SignRequest testSignRequest = signRequests.get(signRequests.size() - 1);
        assertThat(testSignRequest.getBodyEmail()).isEqualTo(DEFAULT_BODY_EMAIL);
        assertThat(testSignRequest.getSubjectEmail()).isEqualTo(DEFAULT_SUBJECT_EMAIL);
        assertThat(testSignRequest.getSignaturitId()).isEqualTo(DEFAULT_SIGNATURIT_ID);
        assertThat(testSignRequest.getSignaturitDocumentId()).isEqualTo(DEFAULT_SIGNATURIT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void checkBodyEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = signRequestRepository.findAll().size();
        // set the field null
        signRequest.setBodyEmail(null);

        // Create the SignRequest, which fails.
        SignRequestDTO signRequestDTO = signRequestMapper.signRequestToSignRequestDTO(signRequest);

        restSignRequestMockMvc.perform(post("/api/sign-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signRequestDTO)))
                .andExpect(status().isBadRequest());

        List<SignRequest> signRequests = signRequestRepository.findAll();
        assertThat(signRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = signRequestRepository.findAll().size();
        // set the field null
        signRequest.setSubjectEmail(null);

        // Create the SignRequest, which fails.
        SignRequestDTO signRequestDTO = signRequestMapper.signRequestToSignRequestDTO(signRequest);

        restSignRequestMockMvc.perform(post("/api/sign-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signRequestDTO)))
                .andExpect(status().isBadRequest());

        List<SignRequest> signRequests = signRequestRepository.findAll();
        assertThat(signRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignRequests() throws Exception {
        // Initialize the database
        signRequestRepository.saveAndFlush(signRequest);

        // Get all the signRequests
        restSignRequestMockMvc.perform(get("/api/sign-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].bodyEmail").value(hasItem(DEFAULT_BODY_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].subjectEmail").value(hasItem(DEFAULT_SUBJECT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].signaturitId").value(hasItem(DEFAULT_SIGNATURIT_ID.toString())))
                .andExpect(jsonPath("$.[*].signaturitDocumentId").value(hasItem(DEFAULT_SIGNATURIT_DOCUMENT_ID.toString())));
    }

    @Test
    @Transactional
    public void getSignRequest() throws Exception {
        // Initialize the database
        signRequestRepository.saveAndFlush(signRequest);

        // Get the signRequest
        restSignRequestMockMvc.perform(get("/api/sign-requests/{id}", signRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signRequest.getId().intValue()))
            .andExpect(jsonPath("$.bodyEmail").value(DEFAULT_BODY_EMAIL.toString()))
            .andExpect(jsonPath("$.subjectEmail").value(DEFAULT_SUBJECT_EMAIL.toString()))
            .andExpect(jsonPath("$.signaturitId").value(DEFAULT_SIGNATURIT_ID.toString()))
            .andExpect(jsonPath("$.signaturitDocumentId").value(DEFAULT_SIGNATURIT_DOCUMENT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignRequest() throws Exception {
        // Get the signRequest
        restSignRequestMockMvc.perform(get("/api/sign-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignRequest() throws Exception {
        // Initialize the database
        signRequestRepository.saveAndFlush(signRequest);
        int databaseSizeBeforeUpdate = signRequestRepository.findAll().size();

        // Update the signRequest
        SignRequest updatedSignRequest = signRequestRepository.findOne(signRequest.getId());
        updatedSignRequest
                .bodyEmail(UPDATED_BODY_EMAIL)
                .subjectEmail(UPDATED_SUBJECT_EMAIL)
                .signaturitId(UPDATED_SIGNATURIT_ID)
                .signaturitDocumentId(UPDATED_SIGNATURIT_DOCUMENT_ID);
        SignRequestDTO signRequestDTO = signRequestMapper.signRequestToSignRequestDTO(updatedSignRequest);

        restSignRequestMockMvc.perform(put("/api/sign-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signRequestDTO)))
                .andExpect(status().isOk());

        // Validate the SignRequest in the database
        List<SignRequest> signRequests = signRequestRepository.findAll();
        assertThat(signRequests).hasSize(databaseSizeBeforeUpdate);
        SignRequest testSignRequest = signRequests.get(signRequests.size() - 1);
        assertThat(testSignRequest.getBodyEmail()).isEqualTo(UPDATED_BODY_EMAIL);
        assertThat(testSignRequest.getSubjectEmail()).isEqualTo(UPDATED_SUBJECT_EMAIL);
        assertThat(testSignRequest.getSignaturitId()).isEqualTo(UPDATED_SIGNATURIT_ID);
        assertThat(testSignRequest.getSignaturitDocumentId()).isEqualTo(UPDATED_SIGNATURIT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void deleteSignRequest() throws Exception {
        // Initialize the database
        signRequestRepository.saveAndFlush(signRequest);
        int databaseSizeBeforeDelete = signRequestRepository.findAll().size();

        // Get the signRequest
        restSignRequestMockMvc.perform(delete("/api/sign-requests/{id}", signRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SignRequest> signRequests = signRequestRepository.findAll();
        assertThat(signRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
