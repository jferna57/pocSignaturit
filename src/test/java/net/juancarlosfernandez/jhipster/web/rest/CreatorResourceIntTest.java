package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.Creator;
import net.juancarlosfernandez.jhipster.repository.CreatorRepository;
import net.juancarlosfernandez.jhipster.service.dto.CreatorDTO;
import net.juancarlosfernandez.jhipster.service.mapper.CreatorMapper;

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
 * Test class for the CreatorResource REST controller.
 *
 * @see CreatorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class CreatorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private CreatorRepository creatorRepository;

    @Inject
    private CreatorMapper creatorMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCreatorMockMvc;

    private Creator creator;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CreatorResource creatorResource = new CreatorResource();
        ReflectionTestUtils.setField(creatorResource, "creatorRepository", creatorRepository);
        ReflectionTestUtils.setField(creatorResource, "creatorMapper", creatorMapper);
        this.restCreatorMockMvc = MockMvcBuilders.standaloneSetup(creatorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Creator createEntity(EntityManager em) {
        Creator creator = new Creator()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL);
        return creator;
    }

    @Before
    public void initTest() {
        creator = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreator() throws Exception {
        int databaseSizeBeforeCreate = creatorRepository.findAll().size();

        // Create the Creator
        CreatorDTO creatorDTO = creatorMapper.creatorToCreatorDTO(creator);

        restCreatorMockMvc.perform(post("/api/creators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(creatorDTO)))
                .andExpect(status().isCreated());

        // Validate the Creator in the database
        List<Creator> creators = creatorRepository.findAll();
        assertThat(creators).hasSize(databaseSizeBeforeCreate + 1);
        Creator testCreator = creators.get(creators.size() - 1);
        assertThat(testCreator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCreator.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorRepository.findAll().size();
        // set the field null
        creator.setName(null);

        // Create the Creator, which fails.
        CreatorDTO creatorDTO = creatorMapper.creatorToCreatorDTO(creator);

        restCreatorMockMvc.perform(post("/api/creators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(creatorDTO)))
                .andExpect(status().isBadRequest());

        List<Creator> creators = creatorRepository.findAll();
        assertThat(creators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorRepository.findAll().size();
        // set the field null
        creator.setEmail(null);

        // Create the Creator, which fails.
        CreatorDTO creatorDTO = creatorMapper.creatorToCreatorDTO(creator);

        restCreatorMockMvc.perform(post("/api/creators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(creatorDTO)))
                .andExpect(status().isBadRequest());

        List<Creator> creators = creatorRepository.findAll();
        assertThat(creators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCreators() throws Exception {
        // Initialize the database
        creatorRepository.saveAndFlush(creator);

        // Get all the creators
        restCreatorMockMvc.perform(get("/api/creators?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(creator.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getCreator() throws Exception {
        // Initialize the database
        creatorRepository.saveAndFlush(creator);

        // Get the creator
        restCreatorMockMvc.perform(get("/api/creators/{id}", creator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCreator() throws Exception {
        // Get the creator
        restCreatorMockMvc.perform(get("/api/creators/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreator() throws Exception {
        // Initialize the database
        creatorRepository.saveAndFlush(creator);
        int databaseSizeBeforeUpdate = creatorRepository.findAll().size();

        // Update the creator
        Creator updatedCreator = creatorRepository.findOne(creator.getId());
        updatedCreator
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL);
        CreatorDTO creatorDTO = creatorMapper.creatorToCreatorDTO(updatedCreator);

        restCreatorMockMvc.perform(put("/api/creators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(creatorDTO)))
                .andExpect(status().isOk());

        // Validate the Creator in the database
        List<Creator> creators = creatorRepository.findAll();
        assertThat(creators).hasSize(databaseSizeBeforeUpdate);
        Creator testCreator = creators.get(creators.size() - 1);
        assertThat(testCreator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCreator.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deleteCreator() throws Exception {
        // Initialize the database
        creatorRepository.saveAndFlush(creator);
        int databaseSizeBeforeDelete = creatorRepository.findAll().size();

        // Get the creator
        restCreatorMockMvc.perform(delete("/api/creators/{id}", creator.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Creator> creators = creatorRepository.findAll();
        assertThat(creators).hasSize(databaseSizeBeforeDelete - 1);
    }
}
