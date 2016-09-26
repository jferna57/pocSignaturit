package net.juancarlosfernandez.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.juancarlosfernandez.jhipster.domain.SignRequest;

import net.juancarlosfernandez.jhipster.repository.SignRequestRepository;
import net.juancarlosfernandez.jhipster.web.rest.util.HeaderUtil;
import net.juancarlosfernandez.jhipster.web.rest.util.PaginationUtil;
import net.juancarlosfernandez.jhipster.service.dto.SignRequestDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignRequestMapper;
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
 * REST controller for managing SignRequest.
 */
@RestController
@RequestMapping("/api")
public class SignRequestResource {

    private final Logger log = LoggerFactory.getLogger(SignRequestResource.class);
        
    @Inject
    private SignRequestRepository signRequestRepository;

    @Inject
    private SignRequestMapper signRequestMapper;

    /**
     * POST  /sign-requests : Create a new signRequest.
     *
     * @param signRequestDTO the signRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signRequestDTO, or with status 400 (Bad Request) if the signRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sign-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignRequestDTO> createSignRequest(@Valid @RequestBody SignRequestDTO signRequestDTO) throws URISyntaxException {
        log.debug("REST request to save SignRequest : {}", signRequestDTO);
        if (signRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "idexists", "A new signRequest cannot already have an ID")).body(null);
        }
        SignRequest signRequest = signRequestMapper.signRequestDTOToSignRequest(signRequestDTO);
        signRequest = signRequestRepository.save(signRequest);
        SignRequestDTO result = signRequestMapper.signRequestToSignRequestDTO(signRequest);
        return ResponseEntity.created(new URI("/api/sign-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("signRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sign-requests : Updates an existing signRequest.
     *
     * @param signRequestDTO the signRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signRequestDTO,
     * or with status 400 (Bad Request) if the signRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the signRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sign-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignRequestDTO> updateSignRequest(@Valid @RequestBody SignRequestDTO signRequestDTO) throws URISyntaxException {
        log.debug("REST request to update SignRequest : {}", signRequestDTO);
        if (signRequestDTO.getId() == null) {
            return createSignRequest(signRequestDTO);
        }
        SignRequest signRequest = signRequestMapper.signRequestDTOToSignRequest(signRequestDTO);
        signRequest = signRequestRepository.save(signRequest);
        SignRequestDTO result = signRequestMapper.signRequestToSignRequestDTO(signRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signRequest", signRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sign-requests : get all the signRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of signRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sign-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SignRequestDTO>> getAllSignRequests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SignRequests");
        Page<SignRequest> page = signRequestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sign-requests");
        return new ResponseEntity<>(signRequestMapper.signRequestsToSignRequestDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /sign-requests/:id : get the "id" signRequest.
     *
     * @param id the id of the signRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signRequestDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sign-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignRequestDTO> getSignRequest(@PathVariable Long id) {
        log.debug("REST request to get SignRequest : {}", id);
        SignRequest signRequest = signRequestRepository.findOne(id);
        SignRequestDTO signRequestDTO = signRequestMapper.signRequestToSignRequestDTO(signRequest);
        return Optional.ofNullable(signRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sign-requests/:id : delete the "id" signRequest.
     *
     * @param id the id of the signRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sign-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSignRequest(@PathVariable Long id) {
        log.debug("REST request to delete SignRequest : {}", id);
        signRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signRequest", id.toString())).build();
    }

}
