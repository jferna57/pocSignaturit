package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Contract;
import net.juancarlosfernandez.jhipster.domain.Signer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Signer entity.
 */
@SuppressWarnings("unused")
public interface SignerRepository extends JpaRepository<Signer,Long> {

    List<Signer> findByContract(Contract contract);

}
