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
 * are handled in other classes (Student and Module), not directly within Grade.
 */
@Entity
public class Grade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int score;
  private String academicYear;

  @ManyToOne
  private Student student;

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
   * @param id the new ID for this grade
   */
  public void setId(Long id) {
    this.id = id;
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
   * @param score the new score value
   */
  public void setScore(int score) {
    this.score = score;
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
   * @param academicYear the academic year string (e.g., "2024/2025")
   */
  public void setAcademicYear(String academicYear) {
    this.academicYear = academicYear;
  }

  /**
   * Returns the student to whom this grade belongs.
   *
   * @return the associated Student
   */
  public Student getStudent() {
    return student;
  }

  /**
   * Associates this grade with a particular student.
   *
   * @param student the student to link to this grade
   */
  public void setStudent(Student student) {
    this.student = student;
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
   * @param module the module to link to this grade
   */
  public void setModule(Module module) {
    this.module = module;
  }

  /**
   * Returns a string representation of this grade, including ID, score, academic
   * year, and related module information.
   *
   * @return a string describing this grade
   */
  @Override
  public String toString() {
    return "Grade{id=" + id
        + ", score=" + score
        + ", academicYear='" + academicYear + '\''
        + ", moduleCode=" + (module != null ? module.getCode() : "null")
        + ", studentId=" + (student != null ? student.getId() : "null")
        + '}';
  }
}