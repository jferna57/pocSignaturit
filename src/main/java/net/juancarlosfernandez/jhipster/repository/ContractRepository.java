package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Contract;

import net.juancarlosfernandez.jhipster.domain.enumeration.Status;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
public interface ContractRepository extends JpaRepository<Contract,Long> {

    List<Contract> findByStatus(Status status);

}
