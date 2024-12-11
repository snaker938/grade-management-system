package uk.ac.ucl.comp0010.controller;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller for managing Student entities.
 * Provides endpoints to view and update student information.
 */
@RestController
@RequestMapping("/students")
public final class StudentController {

  /** Repository for Student entities. */
  private final StudentRepository studentRepository;

  /**
   * Constructs a StudentController with the required repository.
   *
   * @param studRepo the repository for Student entities
   */
  public StudentController(final StudentRepository studRepo) {
    this.studentRepository = studRepo;
  }

  /**
   * Updates the fields of an existing Student.
   * Only non-null fields in the provided 'updated' object will be used
   * to update the existing Student's fields.
   *
   * @param id      the ID of the student to update
   * @param updated a Student object containing updated fields (e.g.,
   *                firstName, lastName, username, email)
   * @return OK with the updated Student if the student exists,
   *         or NOT_FOUND if no such student is found
   */
  @PutMapping("/{id}")
  public ResponseEntity<Student> updateStudent(
      @PathVariable final Long id,
      @RequestBody final Student updated) {
    Optional<Student> opt = studentRepository.findById(id);
    if (!opt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Student s = opt.get();
    if (updated.getFirstName() != null) {
      s.setFirstName(updated.getFirstName());
    }
    if (updated.getLastName() != null) {
      s.setLastName(updated.getLastName());
    }
    if (updated.getUsername() != null) {
      s.setUsername(updated.getUsername());
    }
    if (updated.getEmail() != null) {
      s.setEmail(updated.getEmail());
    }

    studentRepository.save(s);
    return ResponseEntity.ok(s);
  }
}
