package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterSampleApplicationApp;
import io.github.jhipster.application.domain.Keyword;
import io.github.jhipster.application.repository.KeywordRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link KeywordResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class KeywordResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restKeywordMockMvc;

    private Keyword keyword;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KeywordResource keywordResource = new KeywordResource(keywordRepository);
        this.restKeywordMockMvc = MockMvcBuilders.standaloneSetup(keywordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Keyword createEntity() {
        Keyword keyword = new Keyword()
            .name(DEFAULT_NAME);
        return keyword;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Keyword createUpdatedEntity() {
        Keyword keyword = new Keyword()
            .name(UPDATED_NAME);
        return keyword;
    }

    @BeforeEach
    public void initTest() {
        keywordRepository.deleteAll();
        keyword = createEntity();
    }

    @Test
    public void createKeyword() throws Exception {
        int databaseSizeBeforeCreate = keywordRepository.findAll().size();

        // Create the Keyword
        restKeywordMockMvc.perform(post("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isCreated());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeCreate + 1);
        Keyword testKeyword = keywordList.get(keywordList.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createKeywordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keywordRepository.findAll().size();

        // Create the Keyword with an existing ID
        keyword.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeywordMockMvc.perform(post("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isBadRequest());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllKeywords() throws Exception {
        // Initialize the database
        keywordRepository.save(keyword);

        // Get all the keywordList
        restKeywordMockMvc.perform(get("/api/keywords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyword.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    public void getKeyword() throws Exception {
        // Initialize the database
        keywordRepository.save(keyword);

        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{id}", keyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(keyword.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingKeyword() throws Exception {
        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateKeyword() throws Exception {
        // Initialize the database
        keywordRepository.save(keyword);

        int databaseSizeBeforeUpdate = keywordRepository.findAll().size();

        // Update the keyword
        Keyword updatedKeyword = keywordRepository.findById(keyword.getId()).get();
        updatedKeyword
            .name(UPDATED_NAME);

        restKeywordMockMvc.perform(put("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKeyword)))
            .andExpect(status().isOk());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeUpdate);
        Keyword testKeyword = keywordList.get(keywordList.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingKeyword() throws Exception {
        int databaseSizeBeforeUpdate = keywordRepository.findAll().size();

        // Create the Keyword

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeywordMockMvc.perform(put("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isBadRequest());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteKeyword() throws Exception {
        // Initialize the database
        keywordRepository.save(keyword);

        int databaseSizeBeforeDelete = keywordRepository.findAll().size();

        // Delete the keyword
        restKeywordMockMvc.perform(delete("/api/keywords/{id}", keyword.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Keyword.class);
        Keyword keyword1 = new Keyword();
        keyword1.setId("id1");
        Keyword keyword2 = new Keyword();
        keyword2.setId(keyword1.getId());
        assertThat(keyword1).isEqualTo(keyword2);
        keyword2.setId("id2");
        assertThat(keyword1).isNotEqualTo(keyword2);
        keyword1.setId(null);
        assertThat(keyword1).isNotEqualTo(keyword2);
    }
}
