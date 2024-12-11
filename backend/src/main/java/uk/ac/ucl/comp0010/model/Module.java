package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;

/**
 * Represents a module in the academic system, maintaining details
 * such as its code, name, capacity (max seats), and whether it is
 * mandatory non-condonable (MNC).
 *
 * Capabilities:
 * - Holding multiple grades associated with students who have taken it.
 * - Holding multiple registrations for students enrolled in it.
 * - Computing an average grade from a given list of grades.
 *
 * Exceptions:
 * - NoGradeAvailableException: Thrown when attempting to compute the average
 * grade if no grades are provided.
 */
@Entity
public final class Module {

  /** The unique code of this module (e.g., "COM0001"). */
  @Id
  private String code;

  /** The name of the module (e.g., "Software Engineering"). */
  private String name;

  /** Indicates if the module is mandatory non-condonable (MNC). */
  private boolean mnc;

  /** The maximum number of seats available in this module. */
  private int maxSeats;

  /**
   * The list of grades associated with this module.
   * Each Grade corresponds to a student's score in this module.
   */
  @OneToMany(mappedBy = "module")
  private List<Grade> grades = new ArrayList<>();

  /**
   * The list of registrations associated with this module.
   * Each Registration corresponds to a student enrolled in this module.
   */
  @OneToMany(mappedBy = "module")
  private List<Registration> registrations = new ArrayList<>();

  /**
   * Returns the unique code of this module.
   *
   * @return the module's code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets the unique code of this module.
   *
   * @param newCode the new module code
   */
  public void setCode(final String newCode) {
    this.code = newCode;
  }

  /**
   * Returns the name of this module.
   *
   * @return the module's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this module.
   *
   * @param newName the new name for the module
   */
  public void setName(final String newName) {
    this.name = newName;
  }

  /**
   * Indicates whether this module is MNC.
   *
   * @return true if the module is MNC, false otherwise
   */
  public boolean isMnc() {
    return mnc;
  }

  /**
   * Sets whether this module is MNC.
   *
   * @param newMnc true if the module is MNC, false otherwise
   */
  public void setMnc(final boolean newMnc) {
    this.mnc = newMnc;
  }

  /**
   * Returns the list of grades associated with this module.
   *
   * @return a list of Grade objects related to this module
   */
  public List<Grade> getGrades() {
    return grades;
  }

  /**
   * Sets the list of grades associated with this module.
   *
   * @param newGrades a list of Grade objects
   */
  public void setGrades(final List<Grade> newGrades) {
    this.grades = newGrades;
  }

  /**
   * Returns the list of registrations associated with this module.
   *
   * @return a list of Registration objects related to this module
   */
  public List<Registration> getRegistrations() {
    return registrations;
  }

  /**
   * Sets the list of registrations associated with this module.
   *
   * @param newRegistrations a list of Registration objects
   */
  public void setRegistrations(final List<Registration> newRegistrations) {
    this.registrations = newRegistrations;
  }

  /**
   * Computes the average grade from a list of grades for this module.
   *
   * @param newGrades the list of grades to compute the average from
   * @return the computed average as a float
   * @throws NoGradeAvailableException if the provided list is null or empty
   */
  public float computeAverageGrade(final List<Grade> newGrades)
      throws NoGradeAvailableException {
    if (newGrades == null || newGrades.isEmpty()) {
      throw new NoGradeAvailableException(
        "No grades available for this module");
    }
    int sum = newGrades.stream().mapToInt(Grade::getScore).sum();
    return (float) sum / newGrades.size();
  }

  /**
   * Returns the maximum number of seats available in the module.
   *
   * @return the maximum number of seats
   */
  public int getMaxSeats() {
    return maxSeats;
  }

  /**
   * Sets the maximum number of seats available for the module.
   *
   * @param newMaxSeats the maximum number of seats to set
   */
  public void setMaxSeats(final int newMaxSeats) {
    this.maxSeats = newMaxSeats;
  }

  /**
   * Returns the number of students currently enrolled in the module.
   *
   * @return the number of enrolled students,
   * or 0 if there are no registrations.
   */
  public int getEnrolledCount() {
    return registrations != null ? registrations.size() : 0;
  }

  /**
   * Returns a string representation of this module, including its code,
   * name, MNC status, and the counts of associated grades and registrations.
   *
   * @return a string describing this module
   */
  @Override
  public String toString() {
    return "Module{code='" + code + '\''
        + ", name='" + name + '\''
        + ", mnc=" + mnc
        + ", gradesCount=" + (grades != null ? grades.size() : 0)
        + ", registrationsCount="
        + (registrations != null ? registrations.size() : 0)
        + '}';
  }
}
