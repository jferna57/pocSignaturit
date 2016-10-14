package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.PocSignaturitApp;

import net.juancarlosfernandez.jhipster.domain.Preferences;
import net.juancarlosfernandez.jhipster.repository.PreferencesRepository;

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
 * Test class for the PreferencesResource REST controller.
 *
 * @see PreferencesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocSignaturitApp.class)
public class PreferencesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private PreferencesRepository preferencesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPreferencesMockMvc;

    private Preferences preferences;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PreferencesResource preferencesResource = new PreferencesResource();
        ReflectionTestUtils.setField(preferencesResource, "preferencesRepository", preferencesRepository);
        this.restPreferencesMockMvc = MockMvcBuilders.standaloneSetup(preferencesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createEntity(EntityManager em) {
        Preferences preferences = new Preferences()
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE);
        return preferences;
    }

    @Before
    public void initTest() {
        preferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreferences() throws Exception {
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();

        // Create the Preferences

        restPreferencesMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferences)))
                .andExpect(status().isCreated());

        // Validate the Preferences in the database
        List<Preferences> preferences = preferencesRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeCreate + 1);
        Preferences testPreferences = preferences.get(preferences.size() - 1);
        assertThat(testPreferences.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPreferences.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferencesRepository.findAll().size();
        // set the field null
        preferences.setName(null);

        // Create the Preferences, which fails.

        restPreferencesMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferences)))
                .andExpect(status().isBadRequest());

        List<Preferences> preferences = preferencesRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferencesRepository.findAll().size();
        // set the field null
        preferences.setValue(null);

        // Create the Preferences, which fails.

        restPreferencesMockMvc.perform(post("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferences)))
                .andExpect(status().isBadRequest());

        List<Preferences> preferences = preferencesRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferences
        restPreferencesMockMvc.perform(get("/api/preferences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preferences.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPreferences() throws Exception {
        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        int databaseSizeBeforeUpdate = preferencesRepository.findAll().size();

        // Update the preferences
        Preferences updatedPreferences = preferencesRepository.findOne(preferences.getId());
        updatedPreferences
                .name(UPDATED_NAME)
                .value(UPDATED_VALUE);

        restPreferencesMockMvc.perform(put("/api/preferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPreferences)))
                .andExpect(status().isOk());

        // Validate the Preferences in the database
        List<Preferences> preferences = preferencesRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeUpdate);
        Preferences testPreferences = preferences.get(preferences.size() - 1);
        assertThat(testPreferences.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPreferences.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deletePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        int databaseSizeBeforeDelete = preferencesRepository.findAll().size();

        // Get the preferences
        restPreferencesMockMvc.perform(delete("/api/preferences/{id}", preferences.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Preferences> preferences = preferencesRepository.findAll();
        assertThat(preferences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
