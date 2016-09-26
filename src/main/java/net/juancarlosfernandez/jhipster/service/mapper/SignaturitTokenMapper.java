package net.juancarlosfernandez.jhipster.service.mapper;

import net.juancarlosfernandez.jhipster.domain.*;
import net.juancarlosfernandez.jhipster.service.dto.SignaturitTokenDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SignaturitToken and its DTO SignaturitTokenDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SignaturitTokenMapper {

    SignaturitTokenDTO signaturitTokenToSignaturitTokenDTO(SignaturitToken signaturitToken);

    List<SignaturitTokenDTO> signaturitTokensToSignaturitTokenDTOs(List<SignaturitToken> signaturitTokens);

    SignaturitToken signaturitTokenDTOToSignaturitToken(SignaturitTokenDTO signaturitTokenDTO);

    List<SignaturitToken> signaturitTokenDTOsToSignaturitTokens(List<SignaturitTokenDTO> signaturitTokenDTOs);
}
