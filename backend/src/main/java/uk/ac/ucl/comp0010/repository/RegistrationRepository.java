package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ucl.comp0010.model.Registration;

/**
 * Repository interface for managing Registration entities.
 *
 * Registration data can be accessed at /registrations
 */
@RepositoryRestResource(path = "registrations",
            collectionResourceRel = "registrations",
            itemResourceRel = "registration")
public interface RegistrationRepository
  extends CrudRepository<Registration, Long> {
  // Additional custom queries can be defined here if needed.
}
