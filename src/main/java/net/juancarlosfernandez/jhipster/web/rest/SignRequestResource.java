package net.juancarlosfernandez.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.juancarlosfernandez.jhipster.domain.Contract;
import net.juancarlosfernandez.jhipster.domain.SignRequest;

import net.juancarlosfernandez.jhipster.domain.SignaturitToken;
import net.juancarlosfernandez.jhipster.domain.Signer;
import net.juancarlosfernandez.jhipster.repository.ContractRepository;
import net.juancarlosfernandez.jhipster.repository.SignRequestRepository;
import net.juancarlosfernandez.jhipster.repository.SignaturitTokenRepository;
import net.juancarlosfernandez.jhipster.repository.SignerRepository;
import net.juancarlosfernandez.jhipster.service.dto.ContractDTO;
import net.juancarlosfernandez.jhipster.web.rest.util.HeaderUtil;
import net.juancarlosfernandez.jhipster.web.rest.util.PaginationUtil;
import net.juancarlosfernandez.jhipster.service.dto.SignRequestDTO;
import net.juancarlosfernandez.jhipster.service.mapper.SignRequestMapper;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.juancarlosfernandez.jhipster.domain.enumeration.Status;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import com.signaturit.api.java_sdk.Client;

import static java.time.Instant.now;

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

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private SignaturitTokenRepository signaturitTokenRepository;

    @Inject
    private SignerRepository signerRepository;

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
    public ResponseEntity<SignRequestDTO> createSignRequest(@Valid @RequestBody SignRequestDTO signRequestDTO) throws URISyntaxException, IOException {
        log.debug("REST request to save SignRequest : {}", signRequestDTO);

        if (signRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "idexists", "A new signRequest cannot already have an ID")).body(null);
        }

        // Check if contract exists.
        Long contractId = signRequestDTO.getContractId();
        if ( contractId == null )
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "contractNotDefined", "A signRequest cannot be signed whithout a contract")).body(null);
        Contract contract = contractRepository.findOne(contractId);

        // Check if contract has signers.
        List<Signer> signers = signerRepository.findByContract(contract);
        if( signers == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "signersNotDefined", "A signRequest cannot be signed whithout a list of signers")).body(null);

        // Check if signaturit token is established
        if (signaturitTokenRepository.findAll().size()!=1)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "tokenNotDefined", "You must define a signaturit token first")).body(null);

        // Check if the contract document is established
        byte[] document = contract.getDocument();
        if(document==null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "documentToSignNotDefined", "You must define a signaturit token first")).body(null);

        String token = signaturitTokenRepository.findAll().get(0).getToken();

        Client client = new Client(token);

        // Get the document to be signed
        File documentToSigned = new File(contract.getContractName()+now().toString());
        FileUtils.writeByteArrayToFile(documentToSigned, document);

        // TODO: Delete the document

        // Add to the arrayList
        ArrayList<File> filePath = new ArrayList<File>();
        filePath.add(documentToSigned);

        // Add contract's signers emails and name
        ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String,Object>>();

        for (Signer signer : signers) {
            HashMap<String, Object> recipient = new HashMap<String, Object>();
            recipient.put("email", signer.getEmail());
            recipient.put("fullname", signer.getName());
            recipients.add(recipient);
        }

        // Add subject and body to the email
        HashMap<String, Object> options= new HashMap<String, Object>();
        options.put("subject", signRequestDTO.getSubjectEmail());
        options.put("body", signRequestDTO.getBodyEmail());

        Response response = client.createSignature(filePath, recipients);
        String jsonData = response.body().string();

        // Recover the signaturitId and save into the signRequestDTO
        String signatuitId = "" ;
        try {

            JSONObject jObject = new JSONObject(jsonData);
            signatuitId = jObject.get("id").toString();

        } catch (JSONException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signRequest", "signaturitIdNotDefined", "The signaturit service doesn't respond with a correct Id")).body(null);
        }

        signRequestDTO.setSignaturitId(signatuitId);

        SignRequest signRequest = signRequestMapper.signRequestDTOToSignRequest(signRequestDTO);
        signRequest = signRequestRepository.save(signRequest);

        // Change the contract status to PENDING
        contract.setStatus(Status.PENDING);
        contractRepository.save(contract);

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
    public ResponseEntity<SignRequestDTO> updateSignRequest(@Valid @RequestBody SignRequestDTO signRequestDTO) throws URISyntaxException, IOException {
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
