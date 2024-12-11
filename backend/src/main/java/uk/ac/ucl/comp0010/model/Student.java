package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;

/**
 * Represents a student enrolled in various modules.
 *
 * A Student:
 * - Can register for multiple modules.
 * - Can have multiple grades, each associated with a module and an academic
 * year.
 * - Can retrieve a specific grade if one has been recorded for a registered
 * module.
 * - Can compute the average of all recorded grades.
 *
 * Throws:
 * - NoRegistrationException if accessing or adding a grade for a module the
 * student is not registered in.
 * - NoGradeAvailableException if trying to get or compute averages when no
 * grades are available.
 */
@Entity
public final class Student {

  /** The unique identifier of this student. */
  @Id
  private Long id;

  /** The first name of the student. */
  private String firstName;

  /** The last name of the student. */
  private String lastName;

  /** The username of the student. */
  private String username;

  /** The email of the student. */
  private String email;

  /**
   * The list of grades associated with this student.
   * Each Grade corresponds to a module score and academic year.
   */
  @OneToMany(mappedBy = "student")
  private List<Grade> grades = new ArrayList<>();

  /**
   * The list of registrations associated with this student.
   * Each Registration corresponds to a module enrollment.
   */
  @OneToMany(mappedBy = "student")
  private List<Registration> registrations = new ArrayList<>();

  /**
   * Returns the unique identifier of this student.
   *
   * @return the student's ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this student.
   *
   * @param newId the new ID for this student
   */
  public void setId(final Long newId) {
    this.id = newId;
  }

  /**
   * Returns the first name of this student.
   *
   * @return the student's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of this student.
   *
   * @param newFirstName the new first name for this student
   */
  public void setFirstName(final String newFirstName) {
    this.firstName = newFirstName;
  }

  /**
   * Returns the last name of this student.
   *
   * @return the student's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this student.
   *
   * @param newLastName the new last name for this student
   */
  public void setLastName(final String newLastName) {
    this.lastName = newLastName;
  }

  /**
   * Returns the username of this student.
   *
   * @return the student's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of this student.
   *
   * @param newUsername the new username for this student
   */
  public void setUsername(final String newUsername) {
    this.username = newUsername;
  }

  /**
   * Returns the email of this student.
   *
   * @return the student's email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of this student.
   *
   * @param newEmail the new email for this student
   */
  public void setEmail(final String newEmail) {
    this.email = newEmail;
  }

  /**
   * Registers this student for the given module by creating a new Registration.
   *
   * @param newModule the module to register
   */
  public void registerModule(final Module newModule) {
    Registration r = new Registration();
    r.setStudent(this);
    r.setModule(newModule);
    registrations.add(r);
  }

  /**
   * Adds a new grade for this student if they are registered for the module
   * associated with the grade.
   *
   * @param newGrade the grade to add
   * @throws NoRegistrationException if the student is not registered for the
   *                                 module in the given grade
   */
  public void addGrade(final Grade newGrade) throws NoRegistrationException {
    boolean registered = registrations.stream()
        .anyMatch(reg -> reg.getModule().getCode()
            .equals(newGrade.getModule().getCode()));

    if (!registered) {
      throw new NoRegistrationException("Not registered for module");
    }

    newGrade.setStudent(this);
    grades.add(newGrade);
  }

  /**
   * Retrieves the grade for the specified module.
   *
   * @param newModule the module for which to retrieve the grade
   * @return the recorded grade for the specified module
   * @throws NoRegistrationException   if the student is not registered for
   *                                   the given module
   * @throws NoGradeAvailableException if the student is registered but no grade
   *                                   has been recorded
   */
  public Grade getGrade(final Module newModule)
      throws NoRegistrationException, NoGradeAvailableException {
    boolean registered = registrations.stream()
        .anyMatch(reg -> reg.getModule().getCode()
            .equals(newModule.getCode()));
    if (!registered) {
      throw new NoRegistrationException("Not registered for this module");
    }

    return grades.stream()
        .filter(g -> g.getModule().getCode().equals(newModule.getCode()))
        .findFirst()
        .orElseThrow(() -> new NoGradeAvailableException("No grade available"));
  }


  /**
 * Returns the list of grades associated with this student.
 *
 * @return a list of Grade objects related to this student
 */
  public List<Grade> getGrades() {
    return grades;
  }




  /**
   * Computes the average of all recorded grades for this student.
   *
   * @return the average of all grades as a float
   * @throws NoGradeAvailableException if the student has no recorded grades
   */
  public float computeAverage() throws NoGradeAvailableException {
    if (grades.isEmpty()) {
      throw new NoGradeAvailableException("No grades available");
    }

    int sum = grades.stream()
        .mapToInt(Grade::getScore)
        .sum();

    return (float) sum / grades.size();
  }

  /**
   * Returns the list of registrations associated with this student.
   *
   * @return a list of Registration objects related to this student
   */
  public List<Registration> getRegistrations() {
    return registrations;
  }

  /**
   * Returns a string representation of this student, including ID and
   * basic identifying information.
   *
   * @return a string describing this student
   */
  @Override
  public String toString() {
    return "Student{id=" + id
        + ", firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", username='" + username + '\''
        + ", email='" + email + '\''
        + ", gradesCount=" + grades.size()
        + ", registrationsCount=" + registrations.size()
        + '}';
  }
}
