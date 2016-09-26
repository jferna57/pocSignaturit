package net.juancarlosfernandez.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.juancarlosfernandez.jhipster.domain.SignaturitToken;

import net.juancarlosfernandez.jhipster.repository.SignaturitTokenRepository;
import net.juancarlosfernandez.jhipster.web.rest.util.HeaderUtil;
import net.juancarlosfernandez.jhipster.service.dto.SignaturitTokenDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignaturitTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing SignaturitToken.
 */
@RestController
@RequestMapping("/api")
public class SignaturitTokenResource {

    private final Logger log = LoggerFactory.getLogger(SignaturitTokenResource.class);
        
    @Inject
    private SignaturitTokenRepository signaturitTokenRepository;

    @Inject
    private SignaturitTokenMapper signaturitTokenMapper;

    /**
     * POST  /signaturit-tokens : Create a new signaturitToken.
     *
     * @param signaturitTokenDTO the signaturitTokenDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signaturitTokenDTO, or with status 400 (Bad Request) if the signaturitToken has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/signaturit-tokens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignaturitTokenDTO> createSignaturitToken(@Valid @RequestBody SignaturitTokenDTO signaturitTokenDTO) throws URISyntaxException {
        log.debug("REST request to save SignaturitToken : {}", signaturitTokenDTO);
        if (signaturitTokenDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signaturitToken", "idexists", "A new signaturitToken cannot already have an ID")).body(null);
        }
        SignaturitToken signaturitToken = signaturitTokenMapper.signaturitTokenDTOToSignaturitToken(signaturitTokenDTO);
        signaturitToken = signaturitTokenRepository.save(signaturitToken);
        SignaturitTokenDTO result = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(signaturitToken);
        return ResponseEntity.created(new URI("/api/signaturit-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("signaturitToken", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signaturit-tokens : Updates an existing signaturitToken.
     *
     * @param signaturitTokenDTO the signaturitTokenDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signaturitTokenDTO,
     * or with status 400 (Bad Request) if the signaturitTokenDTO is not valid,
     * or with status 500 (Internal Server Error) if the signaturitTokenDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/signaturit-tokens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignaturitTokenDTO> updateSignaturitToken(@Valid @RequestBody SignaturitTokenDTO signaturitTokenDTO) throws URISyntaxException {
        log.debug("REST request to update SignaturitToken : {}", signaturitTokenDTO);
        if (signaturitTokenDTO.getId() == null) {
            return createSignaturitToken(signaturitTokenDTO);
        }
        SignaturitToken signaturitToken = signaturitTokenMapper.signaturitTokenDTOToSignaturitToken(signaturitTokenDTO);
        signaturitToken = signaturitTokenRepository.save(signaturitToken);
        SignaturitTokenDTO result = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(signaturitToken);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signaturitToken", signaturitTokenDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signaturit-tokens : get all the signaturitTokens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of signaturitTokens in body
     */
    @RequestMapping(value = "/signaturit-tokens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SignaturitTokenDTO> getAllSignaturitTokens() {
        log.debug("REST request to get all SignaturitTokens");
        List<SignaturitToken> signaturitTokens = signaturitTokenRepository.findAll();
        return signaturitTokenMapper.signaturitTokensToSignaturitTokenDTOs(signaturitTokens);
    }

    /**
     * GET  /signaturit-tokens/:id : get the "id" signaturitToken.
     *
     * @param id the id of the signaturitTokenDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signaturitTokenDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/signaturit-tokens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignaturitTokenDTO> getSignaturitToken(@PathVariable Long id) {
        log.debug("REST request to get SignaturitToken : {}", id);
        SignaturitToken signaturitToken = signaturitTokenRepository.findOne(id);
        SignaturitTokenDTO signaturitTokenDTO = signaturitTokenMapper.signaturitTokenToSignaturitTokenDTO(signaturitToken);
        return Optional.ofNullable(signaturitTokenDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signaturit-tokens/:id : delete the "id" signaturitToken.
     *
     * @param id the id of the signaturitTokenDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/signaturit-tokens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSignaturitToken(@PathVariable Long id) {
        log.debug("REST request to delete SignaturitToken : {}", id);
        signaturitTokenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signaturitToken", id.toString())).build();
    }

}
