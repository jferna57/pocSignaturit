package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.SignaturitToken;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SignaturitToken entity.
 */
@SuppressWarnings("unused")
public interface SignaturitTokenRepository extends JpaRepository<SignaturitToken,Long> {

}
