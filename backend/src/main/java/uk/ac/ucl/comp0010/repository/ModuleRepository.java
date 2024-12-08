package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ucl.comp0010.model.Module;

/**
 * Repository interface for managing Module entities.
 * 
 * Module data can be accessed at /modules
 */
@RepositoryRestResource(path = "modules", collectionResourceRel = "modules", itemResourceRel = "module")
public interface ModuleRepository extends CrudRepository<Module, String> {
  // Additional custom queries can be defined here if needed.
}
