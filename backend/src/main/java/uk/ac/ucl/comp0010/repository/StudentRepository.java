package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ucl.comp0010.model.Student;

/**
 * Repository interface for managing Student entities.
 *
 * Student data can be accessed at /students
 */
@RepositoryRestResource(path = "students",
      collectionResourceRel = "students",
      itemResourceRel = "student")
public interface StudentRepository extends CrudRepository<Student, Long> {
  // Additional custom queries can be defined here if needed.
}
