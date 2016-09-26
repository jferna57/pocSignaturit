package net.juancarlosfernandez.jhipster.service.mapper;

import net.juancarlosfernandez.jhipster.domain.*;
import net.juancarlosfernandez.jhipster.service.dto.SignRequestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SignRequest and its DTO SignRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SignRequestMapper {

    @Mapping(source = "contract.id", target = "contractId")
    SignRequestDTO signRequestToSignRequestDTO(SignRequest signRequest);

    List<SignRequestDTO> signRequestsToSignRequestDTOs(List<SignRequest> signRequests);

    @Mapping(source = "contractId", target = "contract")
    SignRequest signRequestDTOToSignRequest(SignRequestDTO signRequestDTO);

    List<SignRequest> signRequestDTOsToSignRequests(List<SignRequestDTO> signRequestDTOs);

    default Contract contractFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}
