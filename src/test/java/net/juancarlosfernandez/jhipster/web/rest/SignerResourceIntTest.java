package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.Signer;
import net.juancarlosfernandez.jhipster.repository.SignerRepository;
import net.juancarlosfernandez.jhipster.service.dto.SignerDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignerMapper;

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
 * Test class for the SignerResource REST controller.
 *
 * @see SignerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class SignerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private SignerRepository signerRepository;

    @Inject
    private SignerMapper signerMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSignerMockMvc;

    private Signer signer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignerResource signerResource = new SignerResource();
        ReflectionTestUtils.setField(signerResource, "signerRepository", signerRepository);
        ReflectionTestUtils.setField(signerResource, "signerMapper", signerMapper);
        this.restSignerMockMvc = MockMvcBuilders.standaloneSetup(signerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signer createEntity(EntityManager em) {
        Signer signer = new Signer()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL);
        return signer;
    }

    @Before
    public void initTest() {
        signer = createEntity(em);
    }

    @Test
    @Transactional
    public void createSigner() throws Exception {
        int databaseSizeBeforeCreate = signerRepository.findAll().size();

        // Create the Signer
        SignerDTO signerDTO = signerMapper.signerToSignerDTO(signer);

        restSignerMockMvc.perform(post("/api/signers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signerDTO)))
                .andExpect(status().isCreated());

        // Validate the Signer in the database
        List<Signer> signers = signerRepository.findAll();
        assertThat(signers).hasSize(databaseSizeBeforeCreate + 1);
        Signer testSigner = signers.get(signers.size() - 1);
        assertThat(testSigner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSigner.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = signerRepository.findAll().size();
        // set the field null
        signer.setName(null);

        // Create the Signer, which fails.
        SignerDTO signerDTO = signerMapper.signerToSignerDTO(signer);

        restSignerMockMvc.perform(post("/api/signers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signerDTO)))
                .andExpect(status().isBadRequest());

        List<Signer> signers = signerRepository.findAll();
        assertThat(signers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = signerRepository.findAll().size();
        // set the field null
        signer.setEmail(null);

        // Create the Signer, which fails.
        SignerDTO signerDTO = signerMapper.signerToSignerDTO(signer);

        restSignerMockMvc.perform(post("/api/signers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signerDTO)))
                .andExpect(status().isBadRequest());

        List<Signer> signers = signerRepository.findAll();
        assertThat(signers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSigners() throws Exception {
        // Initialize the database
        signerRepository.saveAndFlush(signer);

        // Get all the signers
        restSignerMockMvc.perform(get("/api/signers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getSigner() throws Exception {
        // Initialize the database
        signerRepository.saveAndFlush(signer);

        // Get the signer
        restSignerMockMvc.perform(get("/api/signers/{id}", signer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSigner() throws Exception {
        // Get the signer
        restSignerMockMvc.perform(get("/api/signers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSigner() throws Exception {
        // Initialize the database
        signerRepository.saveAndFlush(signer);
        int databaseSizeBeforeUpdate = signerRepository.findAll().size();

        // Update the signer
        Signer updatedSigner = signerRepository.findOne(signer.getId());
        updatedSigner
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL);
        SignerDTO signerDTO = signerMapper.signerToSignerDTO(updatedSigner);

        restSignerMockMvc.perform(put("/api/signers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signerDTO)))
                .andExpect(status().isOk());

        // Validate the Signer in the database
        List<Signer> signers = signerRepository.findAll();
        assertThat(signers).hasSize(databaseSizeBeforeUpdate);
        Signer testSigner = signers.get(signers.size() - 1);
        assertThat(testSigner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSigner.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deleteSigner() throws Exception {
        // Initialize the database
        signerRepository.saveAndFlush(signer);
        int databaseSizeBeforeDelete = signerRepository.findAll().size();

        // Get the signer
        restSignerMockMvc.perform(delete("/api/signers/{id}", signer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Signer> signers = signerRepository.findAll();
        assertThat(signers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
