package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Represents a registration linking a Student to a Module.
 *
 * A Registration:
 * - Associates a specific Student with a specific Module, indicating that the
 * Student is enrolled in that Module.
 * - Is used by the Student to record which Modules they are taking and by the
 * Module to keep track of which Students are enrolled.
 *
 * Note:
 * - No exceptions are thrown directly from Registration as it serves as a
 * simple relationship
 * entity. Validations and exception handling (e.g., NoRegistrationException)
 * occur at the logic level in classes like Student or services that manage
 * these entities.
 */
@Entity
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Student student;

  @ManyToOne
  private Module module;

  /**
   * Returns the unique identifier of this registration.
   *
   * @return the registration's ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this registration.
   *
   * @param id the new ID for this registration
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the student involved in this registration.
   *
   * @return the associated Student
   */
  public Student getStudent() {
    return student;
  }

  /**
   * Associates this registration with a specific student.
   *
   * @param student the student to link to this registration
   */
  public void setStudent(Student student) {
    this.student = student;
  }

  /**
   * Returns the module involved in this registration.
   *
   * @return the associated Module
   */
  public Module getModule() {
    return module;
  }

  /**
   * Associates this registration with a specific module.
   *
   * @param module the module to link to this registration
   */
  public void setModule(Module module) {
    this.module = module;
  }

  /**
   * Returns a string representation of this registration, including its ID
   * and references to the associated student and module.
   *
   * @return a string describing this registration
   */
  @Override
  public String toString() {
    return "Registration{id=" + id
        + ", studentId=" + (student != null ? student.getId() : "null")
        + ", moduleCode=" + (module != null ? module.getCode() : "null")
        + '}';
  }
}
