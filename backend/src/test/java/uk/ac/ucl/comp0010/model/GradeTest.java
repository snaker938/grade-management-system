package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Grade class.
 *
 * The Grade class represents a score associated with a particular Module
 * and an Academic Year. This test verifies:
 * - Setting and retrieving basic fields (id, score, academicYear).
 * - Associating a Grade with a specific Module.
 * - Ensuring that data is stored and retrieved consistently.
 * 
 */
public final class GradeTest {

  private Grade grade;
  private Module module;

  /**
   * Creates a new Grade and Module instance before each test.
   */
  @BeforeEach
  public void setUp() {
    grade = new Grade();
    module = new Module();
    module.setCode("MODTEST");
    module.setName("Test Module");
    module.setMnc(false);
  }

  /**
   * Tests that fields of a Grade (id, score, academicYear) can be set and
   * retrieved correctly.
   */
  @Test
  @DisplayName("Should set and get all Grade fields correctly")
  public void testSetAndGetFields() {
    grade.setId(10L);
    grade.setScore(85);
    grade.setAcademicYear("2024/2025");

    assertAll("All grade fields should match the values that were set",
        () -> assertEquals(10L, grade.getId(), "ID should be 10"),
        () -> assertEquals(85, grade.getScore(), "Score should be 85"),
        () -> assertEquals("2024/2025", grade.getAcademicYear(), "Academic year should be '2024/2025'"));
  }

  /**
   * Tests that a Grade can hold a reference to a Module.
   * This ensures that when a Grade is associated with a module, it returns the
   * correct reference.
   */
  @Test
  @DisplayName("Should associate Grade with a Module correctly")
  public void testAssociateModule() {
    grade.setModule(module);
    assertAll("Module should be associated correctly",
        () -> assertNotNull(grade.getModule(), "Module reference should not be null"),
        () -> assertEquals("MODTEST", grade.getModule().getCode(), "Module code should be MODTEST"),
        () -> assertEquals("Test Module", grade.getModule().getName(), "Module name should be Test Module"),
        () -> assertEquals(false, grade.getModule().isMnc(), "MNC should be false for this test module"));
  }

  /**
   * Tests the toString() method to ensure it returns a non-null string containing
   * Grade details.
   */
  @Test
  @DisplayName("toString() should return a non-null string containing grade details")
  public void testToString() {
    grade.setId(10L);
    grade.setScore(85);
    grade.setAcademicYear("2024/2025");
    grade.setModule(module);

    String gradeString = grade.toString();
    assertNotNull(gradeString, "toString() should not return null");
    // Optionally, assert that the string contains some key details:
    // This ensures that if we rely on toString() for logging or debugging, it's
    // informative.
    assertAll("toString() should contain field values",
        () -> assertEquals(true, gradeString.contains("10"), "Should contain the ID"),
        () -> assertEquals(true, gradeString.contains("85"), "Should contain the Score"),
        () -> assertEquals(true, gradeString.contains("2024/2025"), "Should contain the Academic Year"),
        () -> assertEquals(true, gradeString.contains("MODTEST"), "Should contain the Module code"));
  }
}
