package net.juancarlosfernandez.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.juancarlosfernandez.jhipster.domain.Creator;

import net.juancarlosfernandez.jhipster.repository.CreatorRepository;
import net.juancarlosfernandez.jhipster.web.rest.util.HeaderUtil;
import net.juancarlosfernandez.jhipster.web.rest.util.PaginationUtil;
import net.juancarlosfernandez.jhipster.service.dto.CreatorDTO;
import net.juancarlosfernandez.jhipster.service.mapper.CreatorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Creator.
 */
@RestController
@RequestMapping("/api")
public class CreatorResource {

    private final Logger log = LoggerFactory.getLogger(CreatorResource.class);
        
    @Inject
    private CreatorRepository creatorRepository;

    @Inject
    private CreatorMapper creatorMapper;

    /**
     * POST  /creators : Create a new creator.
     *
     * @param creatorDTO the creatorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creatorDTO, or with status 400 (Bad Request) if the creator has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/creators",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CreatorDTO> createCreator(@Valid @RequestBody CreatorDTO creatorDTO) throws URISyntaxException {
        log.debug("REST request to save Creator : {}", creatorDTO);
        if (creatorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("creator", "idexists", "A new creator cannot already have an ID")).body(null);
        }
        Creator creator = creatorMapper.creatorDTOToCreator(creatorDTO);
        creator = creatorRepository.save(creator);
        CreatorDTO result = creatorMapper.creatorToCreatorDTO(creator);
        return ResponseEntity.created(new URI("/api/creators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("creator", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /creators : Updates an existing creator.
     *
     * @param creatorDTO the creatorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creatorDTO,
     * or with status 400 (Bad Request) if the creatorDTO is not valid,
     * or with status 500 (Internal Server Error) if the creatorDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/creators",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CreatorDTO> updateCreator(@Valid @RequestBody CreatorDTO creatorDTO) throws URISyntaxException {
        log.debug("REST request to update Creator : {}", creatorDTO);
        if (creatorDTO.getId() == null) {
            return createCreator(creatorDTO);
        }
        Creator creator = creatorMapper.creatorDTOToCreator(creatorDTO);
        creator = creatorRepository.save(creator);
        CreatorDTO result = creatorMapper.creatorToCreatorDTO(creator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("creator", creatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /creators : get all the creators.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of creators in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/creators",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CreatorDTO>> getAllCreators(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Creators");
        Page<Creator> page = creatorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/creators");
        return new ResponseEntity<>(creatorMapper.creatorsToCreatorDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /creators/:id : get the "id" creator.
     *
     * @param id the id of the creatorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creatorDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/creators/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CreatorDTO> getCreator(@PathVariable Long id) {
        log.debug("REST request to get Creator : {}", id);
        Creator creator = creatorRepository.findOne(id);
        CreatorDTO creatorDTO = creatorMapper.creatorToCreatorDTO(creator);
        return Optional.ofNullable(creatorDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /creators/:id : delete the "id" creator.
     *
     * @param id the id of the creatorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/creators/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCreator(@PathVariable Long id) {
        log.debug("REST request to delete Creator : {}", id);
        creatorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("creator", id.toString())).build();
    }

}
