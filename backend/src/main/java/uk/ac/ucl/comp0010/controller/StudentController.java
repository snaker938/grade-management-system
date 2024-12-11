package uk.ac.ucl.comp0010.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller for managing Student entities.
 * Provides endpoints to view and update student information.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentRepository studentRepo;

  /**
   * Constructs a StudentController with the required repository.
   *
   * @param studentRepo the repository for Student entities
   */
  public StudentController(StudentRepository studentRepo) {
    this.studentRepo = studentRepo;
  }

  /**
   * Updates the fields of an existing Student.
   * Only non-null fields in the provided 'updated' object will be used to update
   * the existing Student's fields.
   *
   * @param id      the ID of the student to update
   * @param updated a Student object containing updated fields (e.g., firstName,
   *                lastName, username, email)
   * @return OK with the updated Student if the student exists, or NOT_FOUND if no
   *         such student is found
   */
  @PutMapping("/{id}")
  public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updated) {
    Optional<Student> opt = studentRepo.findById(id);
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

    studentRepo.save(s);
    return ResponseEntity.ok(s);
  }
}
