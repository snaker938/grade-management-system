package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;

/**
 * Represents a student within the academic system, maintaining both personal
 * details and academic associations with modules and grades.
 *
 * Capabilities:
 * - Registering for one or more modules.
 * - Holding multiple grades, each tied to a specific module and academic year.
 * - Retrieving a specific grade from a registered module, if one exists.
 * - Computing the overall average grade across all recorded modules.
 *
 * Exceptions:
 * - NoRegistrationException: Thrown when trying to add or access a grade for a
 * module that the student is not registered in.
 * - NoGradeAvailableException: Thrown when attempting to fetch or compute an
 * average grade if no grades are recorded.
 */

@Entity
public class Module {

  @Id
  private String code;
  private String name;
  private boolean mnc;

  @OneToMany(mappedBy = "module")
  private List<Grade> grades = new ArrayList<>();

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
   * @param code the new module code
   */
  public void setCode(String code) {
    this.code = code;
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
   * @param name the new name for the module
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Indicates whether this module is mandatory non-condonable (MNC).
   *
   * @return true if the module is MNC, false otherwise
   */
  public boolean isMnc() {
    return mnc;
  }

  /**
   * Sets whether this module is mandatory non-condonable (MNC).
   *
   * @param mnc true if the module is MNC, false otherwise
   */
  public void setMnc(boolean mnc) {
    this.mnc = mnc;
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
   * @param grades a list of Grade objects
   */
  public void setGrades(List<Grade> grades) {
    this.grades = grades;
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
   * @param registrations a list of Registration objects
   */
  public void setRegistrations(List<Registration> registrations) {
    this.registrations = registrations;
  }

  /**
   * Computes the average grade from a list of grades for this module.
   * 
   * @param grades the list of grades to compute the average from
   * @return the computed average as a float
   * @throws NoGradeAvailableException if the provided list is null or empty
   */
  public float computeAverageGrade(List<Grade> grades) throws NoGradeAvailableException {
    if (grades == null || grades.isEmpty()) {
      throw new NoGradeAvailableException("No grades available for this module");
    }

    int sum = grades.stream().mapToInt(Grade::getScore).sum();
    return (float) sum / grades.size();
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
        + ", registrationsCount=" + (registrations != null ? registrations.size() : 0)
        + '}';
  }
}
