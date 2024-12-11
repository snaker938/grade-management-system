package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Represents a grade awarded to a student for a given module in a specific
 * academic year.
 *
 * A Grade:
 * - Is associated with a particular Student and Module.
 * - Records the student's score for that module.
 * - Includes the academic year during which the grade was awarded.
 *
 * Note:
 * - Average computation and exception handling related to no available grades
 * are handled in other classes (e.g., Student and Module), not directly here.
 */
@Entity
public final class Grade {

  /** The unique identifier of this grade. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The score assigned to this grade. */
  private int score;

  /** The academic year in which the grade was awarded. */
  private String academicYear;

  /** The student who received this grade. */
  @ManyToOne
  private Student student;

  /** The module for which this grade was awarded. */
  @ManyToOne
  private Module module;

  /**
   * Returns the unique identifier of this grade.
   *
   * @return the grade's ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this grade.
   *
   * @param newId the new ID for this grade
   */
  public void setId(final Long newId) {
    this.id = newId;
  }

  /**
   * Returns the score assigned for this grade.
   *
   * @return the score
   */
  public int getScore() {
    return score;
  }

  /**
   * Sets the score for this grade.
   *
   * @param newScore the new score value
   */
  public void setScore(final int newScore) {
    this.score = newScore;
  }

  /**
   * Returns the academic year during which this grade was awarded.
   *
   * @return the academic year string (e.g., "2024/2025")
   */
  public String getAcademicYear() {
    return academicYear;
  }

  /**
   * Sets the academic year for this grade.
   *
   * @param newAcademicYear the academic year string (e.g., "2024/2025")
   */
  public void setAcademicYear(final String newAcademicYear) {
    this.academicYear = newAcademicYear;
  }

  /**
   * Returns the student who received this grade.
   *
   * @return the associated Student
   */
  public Student getStudent() {
    return student;
  }

  /**
   * Associates this grade with a particular student.
   *
   * @param newStudent the student to link to this grade
   */
  public void setStudent(final Student newStudent) {
    this.student = newStudent;
  }

  /**
   * Returns the module for which this grade was awarded.
   *
   * @return the associated Module
   */
  public Module getModule() {
    return module;
  }

  /**
   * Associates this grade with a particular module.
   *
   * @param newModule the module to link to this grade
   */
  public void setModule(final Module newModule) {
    this.module = newModule;
  }

  /**
   * Returns a string representation of this grade, including ID, score,
   * academic year, and related module information.
   *
   * @return a string describing this grade
   */
  @Override
  public String toString() {
    return "Grade{id=" + id
        + ", score=" + score
        + ", academicYear='" + academicYear + '\''
        + ", moduleCode="
        + (module != null ? module.getCode() : "null")
        + ", studentId="
        + (student != null ? student.getId() : "null") + '}';
  }
}
