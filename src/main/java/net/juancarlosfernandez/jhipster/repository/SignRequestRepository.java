package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.SignRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SignRequest entity.
 */
@SuppressWarnings("unused")
public interface SignRequestRepository extends JpaRepository<SignRequest,Long> {

}
