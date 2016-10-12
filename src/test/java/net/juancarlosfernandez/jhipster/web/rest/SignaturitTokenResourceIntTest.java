package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.SignaturitToken;
import net.juancarlosfernandez.jhipster.repository.SignaturitTokenRepository;
import net.juancarlosfernandez.jhipster.service.dto.SignaturitTokenDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignaturitTokenMapper;

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
 * Test class for the SignaturitTokenResource REST controller.
 *
 * @see SignaturitTokenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class SignaturitTokenResourceIntTest {

    private static final String DEFAULT_TOKEN = "AAAAA";
    private static final String UPDATED_TOKEN = "BBBBB";

    @Inject
    private SignaturitTokenRepository signaturitTokenRepository;

    @Inject
    private SignaturitTokenMapper signaturitTokenMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSignaturitTokenMockMvc;

    private SignaturitToken signaturitToken;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignaturitTokenResource signaturitTokenResource = new SignaturitTokenResource();
        ReflectionTestUtils.setField(signaturitTokenResource, "signaturitTokenRepository", signaturitTokenRepository);
        ReflectionTestUtils.setField(signaturitTokenResource, "signaturitTokenMapper", signaturitTokenMapper);
        this.restSignaturitTokenMockMvc = MockMvcBuilders.standaloneSetup(signaturitTokenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignaturitToken createEntity(EntityManager em) {
        SignaturitToken signaturitToken = new SignaturitToken()
                .token(DEFAULT_TOKEN);
        return signaturitToken;
    }

    @Before
    public void initTest() {
        signaturitToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignaturitToken() throws Exception {
        int databaseSizeBeforeCreate = signaturitTokenRepository.findAll().size();

        // Create the SignaturitToken
        SignaturitTokenDTO signaturitTokenDTO = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(signaturitToken);

        restSignaturitTokenMockMvc.perform(post("/api/signaturit-tokens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signaturitTokenDTO)))
                .andExpect(status().isCreated());

        // Validate the SignaturitToken in the database
        List<SignaturitToken> signaturitTokens = signaturitTokenRepository.findAll();
        assertThat(signaturitTokens).hasSize(databaseSizeBeforeCreate + 1);
        SignaturitToken testSignaturitToken = signaturitTokens.get(signaturitTokens.size() - 1);
        assertThat(testSignaturitToken.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    public void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = signaturitTokenRepository.findAll().size();
        // set the field null
        signaturitToken.setToken(null);

        // Create the SignaturitToken, which fails.
        SignaturitTokenDTO signaturitTokenDTO = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(signaturitToken);

        restSignaturitTokenMockMvc.perform(post("/api/signaturit-tokens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signaturitTokenDTO)))
                .andExpect(status().isBadRequest());

        List<SignaturitToken> signaturitTokens = signaturitTokenRepository.findAll();
        assertThat(signaturitTokens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignaturitTokens() throws Exception {
        // Initialize the database
        signaturitTokenRepository.saveAndFlush(signaturitToken);

        // Get all the signaturitTokens
        restSignaturitTokenMockMvc.perform(get("/api/signaturit-tokens?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signaturitToken.getId().intValue())))
                .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getSignaturitToken() throws Exception {
        // Initialize the database
        signaturitTokenRepository.saveAndFlush(signaturitToken);

        // Get the signaturitToken
        restSignaturitTokenMockMvc.perform(get("/api/signaturit-tokens/{id}", signaturitToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signaturitToken.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignaturitToken() throws Exception {
        // Get the signaturitToken
        restSignaturitTokenMockMvc.perform(get("/api/signaturit-tokens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignaturitToken() throws Exception {
        // Initialize the database
        signaturitTokenRepository.saveAndFlush(signaturitToken);
        int databaseSizeBeforeUpdate = signaturitTokenRepository.findAll().size();

        // Update the signaturitToken
        SignaturitToken updatedSignaturitToken = signaturitTokenRepository.findOne(signaturitToken.getId());
        updatedSignaturitToken
                .token(UPDATED_TOKEN);
        SignaturitTokenDTO signaturitTokenDTO = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(updatedSignaturitToken);

        restSignaturitTokenMockMvc.perform(put("/api/signaturit-tokens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signaturitTokenDTO)))
                .andExpect(status().isOk());

        // Validate the SignaturitToken in the database
        List<SignaturitToken> signaturitTokens = signaturitTokenRepository.findAll();
        assertThat(signaturitTokens).hasSize(databaseSizeBeforeUpdate);
        SignaturitToken testSignaturitToken = signaturitTokens.get(signaturitTokens.size() - 1);
        assertThat(testSignaturitToken.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    public void deleteSignaturitToken() throws Exception {
        // Initialize the database
        signaturitTokenRepository.saveAndFlush(signaturitToken);
        int databaseSizeBeforeDelete = signaturitTokenRepository.findAll().size();

        // Get the signaturitToken
        restSignaturitTokenMockMvc.perform(delete("/api/signaturit-tokens/{id}", signaturitToken.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SignaturitToken> signaturitTokens = signaturitTokenRepository.findAll();
        assertThat(signaturitTokens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
