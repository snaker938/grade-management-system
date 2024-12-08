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
public class Student {

  @Id
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;

  @OneToMany(mappedBy = "student")
  private List<Grade> grades = new ArrayList<>();

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
   * @param id the new ID for this student
   */
  public void setId(Long id) {
    this.id = id;
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
   * @param firstName the new first name for this student
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
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
   * @param lastName the new last name for this student
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
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
   * @param username the new username for this student
   */
  public void setUsername(String username) {
    this.username = username;
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
   * @param email the new email for this student
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Registers this student for the given module by creating a new Registration.
   *
   * @param module the module to register
   */
  public void registerModule(Module module) {
    Registration r = new Registration();
    r.setStudent(this);
    r.setModule(module);
    registrations.add(r);
  }

  /**
   * Adds a new grade for this student if they are registered for the module
   * associated with the grade.
   * 
   * @param grade the grade to add
   * @throws NoRegistrationException if the student is not registered for the
   *                                 module in the given grade
   */
  public void addGrade(Grade grade) throws NoRegistrationException {
    boolean registered = registrations.stream()
        .anyMatch(reg -> reg.getModule().getCode().equals(grade.getModule().getCode()));

    if (!registered) {
      throw new NoRegistrationException("Not registered for module");
    }

    grade.setStudent(this);
    grades.add(grade);
  }

  /**
   * Retrieves the grade for the specified module.
   *
   * @param module the module for which to retrieve the grade
   * @return the recorded grade for the specified module
   * @throws NoRegistrationException   if the student is not registered for the
   *                                   given module
   * @throws NoGradeAvailableException if the student is registered but no grade
   *                                   has been recorded
   */
  public Grade getGrade(Module module) throws NoRegistrationException, NoGradeAvailableException {
    boolean registered = registrations.stream()
        .anyMatch(reg -> reg.getModule().getCode().equals(module.getCode()));
    if (!registered) {
      throw new NoRegistrationException("Not registered for this module");
    }

    return grades.stream()
        .filter(g -> g.getModule().getCode().equals(module.getCode()))
        .findFirst()
        .orElseThrow(() -> new NoGradeAvailableException("No grade available"));
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
   * Returns a string representation of this student, including ID and basic
   * identifying information.
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
