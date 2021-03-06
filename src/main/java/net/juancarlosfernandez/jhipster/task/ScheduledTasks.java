package net.juancarlosfernandez.jhipster.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.signaturit.api.java_sdk.Client;
import net.juancarlosfernandez.jhipster.domain.Contract;
import net.juancarlosfernandez.jhipster.domain.Preferences;
import net.juancarlosfernandez.jhipster.domain.SignRequest;
import net.juancarlosfernandez.jhipster.domain.enumeration.Status;
import net.juancarlosfernandez.jhipster.repository.ContractRepository;
import net.juancarlosfernandez.jhipster.repository.PreferencesRepository;
import net.juancarlosfernandez.jhipster.repository.SignRequestRepository;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import okhttp3.Response;


import javax.inject.Inject;

import static java.time.Instant.now;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Inject
    private PreferencesRepository preferencesRepository;

    @Inject
    private SignRequestRepository signRequestRepository;

    @Inject
    private ContractRepository contractRepository;

    /**
     * Every 100 seconds synchronize contract information
     */

    @Scheduled(fixedRate = 30000)
    //@Scheduled(cron = "* */10 * * * *")
    public void syncSignedContracts() throws IOException, JSONException {

        log.info("Start -> Sync Signed Contracts");

        // Check if signaturit token is established
        Optional<Preferences> token = preferencesRepository.findOneByName("token");
        if ( !token.isPresent()){
            log.error("Signaturit Token not defined!");
        } else {
            log.info("Signaturit Token defined!");

            Client client = new Client(token.get().getValue());

            List<Contract> contracts= contractRepository.findByStatus(Status.PENDING);

            for (Contract contract : contracts) {

                log.info("Processing contract: {} status: {} ", contract.getContractName(), contract.getStatus());

                // TODO: Check that the contract have a SignRequest entity.
                if (contract.getSignRequest() != null) {
                    SignRequest signRequest = signRequestRepository.findOne(contract.getSignRequest().getId());
                    Response response = client.getSignature(signRequest.getSignaturitId());
                    String jsonData = response.body().string();

                    JSONObject jObject = new JSONObject(jsonData);
                    JSONArray documents = jObject.getJSONArray("documents");
                    String status = documents.getJSONObject(0).get("status").toString();
                    String documentId = documents.getJSONObject(0).get("id").toString();

                    if (status.equalsIgnoreCase("completed")) {

                        // Get the signed document
                        response = client.downloadSignedDocument(signRequest.getSignaturitId(), documentId);
                        byte[] signedDocument = response.body().bytes();

                        // Save the documentId to this request.
                        signRequest.setSignaturitDocumentId(documentId);
                        signRequestRepository.save(signRequest);

                        // change the status of the contract and set the signed document
                        contract.setStatus(Status.SIGNED);
                        contract.setDocumentSigned(signedDocument);
                        contract.setDocumentSignedContentType("application/pdf");
                        contract.setSignedDate(now().atZone(ZoneId.systemDefault()));
                        contractRepository.save(contract);

                    } else {
                        log.info("Contract {} with signaturitId {} not signed yet. Status {} ",
                            contract.getContractName(),
                            signRequest.getSignaturitId(),
                            status);
                    }
                }

            }

        }

        log.info("End -> Sync Signed Contracts");

    }
}
