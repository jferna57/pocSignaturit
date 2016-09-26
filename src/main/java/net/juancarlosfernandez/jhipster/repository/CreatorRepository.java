package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Creator;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Creator entity.
 */
@SuppressWarnings("unused")
public interface CreatorRepository extends JpaRepository<Creator,Long> {

}
