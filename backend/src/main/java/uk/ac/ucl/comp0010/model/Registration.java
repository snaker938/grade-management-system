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
 * - Associates a specific Student with a specific Module, indicating
 * that the Student is enrolled in that Module.
 * - Used by the Student entity to record which Modules they are taking
 * and by the Module entity to keep track of enrolled Students.
 *
 * Note:
 * - No exceptions are thrown directly from Registration. Validations
 * and exception handling occur at the logic level (in Student or in
 * services that manage these entities).
 */
@Entity
public final class Registration {

  /** The unique identifier of this registration. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The student associated with this registration. */
  @ManyToOne
  private Student student;

  /** The module associated with this registration. */
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
   * @param newId the new ID for this registration
   */
  public void setId(final Long newId) {
    this.id = newId;
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
   * @param newStudent the student to link to this registration
   */
  public void setStudent(final Student newStudent) {
    this.student = newStudent;
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
   * @param newModule the module to link to this registration
   */
  public void setModule(final Module newModule) {
    this.module = newModule;
  }

  /**
   * Returns a string representation of this registration,
   * including its ID and references to the associated student
   * and module.
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
