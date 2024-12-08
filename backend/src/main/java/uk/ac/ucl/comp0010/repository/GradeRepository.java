package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ucl.comp0010.model.Grade;

/**
 * Repository interface for managing Grade entities.
 * 
 * Grade data can be accessed at /grades
 */
@RepositoryRestResource(path = "grades", collectionResourceRel = "grades", itemResourceRel = "grade")
public interface GradeRepository extends CrudRepository<Grade, Long> {
  // Additional custom queries can be defined here if needed.
}
