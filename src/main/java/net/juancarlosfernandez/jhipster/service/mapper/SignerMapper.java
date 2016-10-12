package net.juancarlosfernandez.jhipster.service.mapper;

import net.juancarlosfernandez.jhipster.domain.*;
import net.juancarlosfernandez.jhipster.service.dto.SignerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Signer and its DTO SignerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SignerMapper {

    @Mapping(source = "contract.id", target = "contractId")
    SignerDTO signerToSignerDTO(Signer signer);

    List<SignerDTO> signersToSignerDTOs(List<Signer> signers);

    @Mapping(source = "contractId", target = "contract")
    Signer signerDTOToSigner(SignerDTO signerDTO);

    List<Signer> signerDTOsToSigners(List<SignerDTO> signerDTOs);

    default Contract contractFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}
