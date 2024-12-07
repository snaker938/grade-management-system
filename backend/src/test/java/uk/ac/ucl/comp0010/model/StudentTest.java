package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;

/**
 * Unit tests for the Student class.
 * 
 * Tests cover:
 * - Setting and retrieving basic fields (ID, firstName, lastName, username,
 * email).
 * - Registering modules and adding grades.
 * - Retrieving grades, including when no registration or no grades are
 * available.
 * - Computing the average grade for a student.
 */
class StudentTest {

  private Student student;
  private Module module1;
  private Module module2;

  /**
   * Sets up new Student and Module instances before each test.
   */
  @BeforeEach
  void setUp() {
    student = new Student();
    module1 = new Module();
    module1.setCode("MOD001");
    module2 = new Module();
    module2.setCode("MOD002");
  }

  /**
   * Tests that student fields can be set and retrieved correctly.
   */
  @Test
  @DisplayName("Should set and get all Student fields correctly")
  void testSetAndGetFields() {
    student.setId(1L);
    student.setFirstName("Alice");
    student.setLastName("Smith");
    student.setUsername("asmith");
    student.setEmail("alice.smith@example.com");

    assertAll("All student fields should match the values that were set",
        () -> assertEquals(1L, student.getId(), "ID should be 1"),
        () -> assertEquals("Alice", student.getFirstName(), "First name should be Alice"),
        () -> assertEquals("Smith", student.getLastName(), "Last name should be Smith"),
        () -> assertEquals("asmith", student.getUsername(), "Username should be asmith"),
        () -> assertEquals("alice.smith@example.com", student.getEmail(), "Email should be alice.smith@example.com"));
  }

  /**
   * Tests registering a module, adding a grade for that module,
   * and then retrieving the grade.
   *
   * This covers both registerModule() and addGrade(), as well as getGrade().
   */
  @Test
  @DisplayName("Should register module, add grade, and retrieve that grade")
  void testRegisterModuleAndAddGradeAndGetGrade() throws Exception {
    student.registerModule(module1);

    Grade g = new Grade();
    g.setScore(90);
    g.setModule(module1);
    g.setAcademicYear("2024/2025");
    student.addGrade(g);

    Grade retrieved = student.getGrade(module1);

    assertAll("Grade should be retrievable and match the added grade",
        () -> assertEquals(90, retrieved.getScore(), "Score should be 90"),
        () -> assertEquals("2024/2025", retrieved.getAcademicYear(), "Academic year should be 2024/2025"));
  }

  /**
   * Tests that attempting to get a grade from a module for which
   * the student is not registered throws NoRegistrationException.
   *
   * This checks the behavior of getGrade() when no registration is found.
   */
  @Test
  @DisplayName("Should throw NoRegistrationException if student is not registered in the module")
  void testGetGradeNoRegistration() {
    assertThrows(NoRegistrationException.class, () -> student.getGrade(module1),
        "Should throw NoRegistrationException when not registered in the module");
  }

  /**
   * Tests that attempting to get a grade for a registered module
   * when no grade is available throws NoGradeAvailableException.
   *
   * This checks the behavior of getGrade() when the student is registered but has
   * no grade recorded.
   */
  @Test
  @DisplayName("Should throw NoGradeAvailableException if no grade is recorded for the module")
  void testGetGradeNoGradeAvailable() throws Exception {
    student.registerModule(module1);
    assertThrows(NoGradeAvailableException.class, () -> student.getGrade(module1),
        "Should throw NoGradeAvailableException when no grade is available");
  }

  /**
   * Tests computing the average grade for a student who has grades in multiple
   * modules.
   * Ensures that computeAverage() returns the correct mean value.
   */
  @Test
  @DisplayName("Should compute average grade correctly")
  void testComputeAverage() throws Exception {
    student.registerModule(module1);
    Grade g1 = new Grade();
    g1.setScore(50);
    g1.setModule(module1);
    g1.setAcademicYear("2024/2025");
    student.addGrade(g1);

    student.registerModule(module2);
    Grade g2 = new Grade();
    g2.setScore(100);
    g2.setModule(module2);
    g2.setAcademicYear("2024/2025");
    student.addGrade(g2);

    float avg = student.computeAverage();
    assertEquals(75.0f, avg, "Average of 50 and 100 should be 75.0f");
  }

  /**
   * Tests that attempting to compute the average grade for a student with no
   * grades
   * throws a NoGradeAvailableException.
   */
  @Test
  @DisplayName("Should throw NoGradeAvailableException if no grades are recorded when computing average")
  void testComputeAverageNoGrades() {
    assertThrows(NoGradeAvailableException.class, student::computeAverage,
        "Should throw NoGradeAvailableException when no grades are available");
  }
}
