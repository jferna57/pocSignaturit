package net.juancarlosfernandez.jhipster.service.mapper;

import net.juancarlosfernandez.jhipster.domain.*;
import net.juancarlosfernandez.jhipster.service.dto.ContractDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContractMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    ContractDTO contractToContractDTO(Contract contract);

    List<ContractDTO> contractsToContractDTOs(List<Contract> contracts);

    @Mapping(target = "signers", ignore = true)
    @Mapping(target = "signRequest", ignore = true)
    @Mapping(source = "creatorId", target = "creator")
    Contract contractDTOToContract(ContractDTO contractDTO);

    List<Contract> contractDTOsToContracts(List<ContractDTO> contractDTOs);

    default Creator creatorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Creator creator = new Creator();
        creator.setId(id);
        return creator;
    }
}
