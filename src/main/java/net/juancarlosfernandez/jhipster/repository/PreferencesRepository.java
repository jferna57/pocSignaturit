package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Preferences;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

}
