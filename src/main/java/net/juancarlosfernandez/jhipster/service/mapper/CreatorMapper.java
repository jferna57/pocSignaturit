package net.juancarlosfernandez.jhipster.service.mapper;

import net.juancarlosfernandez.jhipster.domain.*;
import net.juancarlosfernandez.jhipster.service.dto.CreatorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Creator and its DTO CreatorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CreatorMapper {

    CreatorDTO creatorToCreatorDTO(Creator creator);

    List<CreatorDTO> creatorsToCreatorDTOs(List<Creator> creators);

    @Mapping(target = "contracts", ignore = true)
    Creator creatorDTOToCreator(CreatorDTO creatorDTO);

    List<Creator> creatorDTOsToCreators(List<CreatorDTO> creatorDTOs);
}
