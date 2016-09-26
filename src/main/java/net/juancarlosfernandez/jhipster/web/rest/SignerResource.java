package net.juancarlosfernandez.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.juancarlosfernandez.jhipster.domain.Signer;

import net.juancarlosfernandez.jhipster.repository.SignerRepository;
import net.juancarlosfernandez.jhipster.web.rest.util.HeaderUtil;
import net.juancarlosfernandez.jhipster.web.rest.util.PaginationUtil;
import net.juancarlosfernandez.jhipster.service.dto.SignerDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignerMapper;
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
 * REST controller for managing Signer.
 */
@RestController
@RequestMapping("/api")
public class SignerResource {

    private final Logger log = LoggerFactory.getLogger(SignerResource.class);
        
    @Inject
    private SignerRepository signerRepository;

    @Inject
    private SignerMapper signerMapper;

    /**
     * POST  /signers : Create a new signer.
     *
     * @param signerDTO the signerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signerDTO, or with status 400 (Bad Request) if the signer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/signers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignerDTO> createSigner(@Valid @RequestBody SignerDTO signerDTO) throws URISyntaxException {
        log.debug("REST request to save Signer : {}", signerDTO);
        if (signerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signer", "idexists", "A new signer cannot already have an ID")).body(null);
        }
        Signer signer = signerMapper.signerDTOToSigner(signerDTO);
        signer = signerRepository.save(signer);
        SignerDTO result = signerMapper.signerToSignerDTO(signer);
        return ResponseEntity.created(new URI("/api/signers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("signer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signers : Updates an existing signer.
     *
     * @param signerDTO the signerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signerDTO,
     * or with status 400 (Bad Request) if the signerDTO is not valid,
     * or with status 500 (Internal Server Error) if the signerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/signers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignerDTO> updateSigner(@Valid @RequestBody SignerDTO signerDTO) throws URISyntaxException {
        log.debug("REST request to update Signer : {}", signerDTO);
        if (signerDTO.getId() == null) {
            return createSigner(signerDTO);
        }
        Signer signer = signerMapper.signerDTOToSigner(signerDTO);
        signer = signerRepository.save(signer);
        SignerDTO result = signerMapper.signerToSignerDTO(signer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signer", signerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signers : get all the signers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of signers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/signers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SignerDTO>> getAllSigners(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Signers");
        Page<Signer> page = signerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signers");
        return new ResponseEntity<>(signerMapper.signersToSignerDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /signers/:id : get the "id" signer.
     *
     * @param id the id of the signerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signerDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/signers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignerDTO> getSigner(@PathVariable Long id) {
        log.debug("REST request to get Signer : {}", id);
        Signer signer = signerRepository.findOne(id);
        SignerDTO signerDTO = signerMapper.signerToSignerDTO(signer);
        return Optional.ofNullable(signerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signers/:id : delete the "id" signer.
     *
     * @param id the id of the signerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/signers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSigner(@PathVariable Long id) {
        log.debug("REST request to delete Signer : {}", id);
        signerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signer", id.toString())).build();
    }

}
