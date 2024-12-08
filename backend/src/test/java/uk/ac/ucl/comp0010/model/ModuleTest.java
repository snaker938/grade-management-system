package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;

/**
 * Unit tests for the Module class.
 *
 * This class tests:
 * - Setting and retrieving module fields (code, name, mnc).
 * - Computing the average grade of a module from a list of Grade objects.
 * - Ensuring that an exception is thrown when no grades are available.
 *
 * Note:
 * - The mnc field (Mandatory Non-Condonable) indicates that the module is
 * essential and cannot be overlooked.
 * - NoGradeAvailableException is thrown if there are no grades at all or if no
 * grade is available for the specific context.
 */
class ModuleTest {

  /**
   * Tests that all fields of a Module can be set and retrieved correctly.
   */
  @Test
  @DisplayName("Should set and get all Module fields correctly")
  void testSetAndGetFields() {
    Module module = new Module();
    module.setCode("MODX");
    module.setName("Engineering");
    module.setMnc(true);

    assertAll("All module fields should match the values that were set",
        () -> assertEquals("MODX", module.getCode(), "Code should be MODX"),
        () -> assertEquals("Engineering", module.getName(), "Name should be Engineering"),
        () -> assertEquals(true, module.isMnc(), "MNC should be true"));
  }

  /**
   * Tests computing the average grade for a module given a list of Grade objects.
   * Ensures that multiple grades are correctly averaged.
   */
  @Test
  @DisplayName("Should compute the average grade correctly from a list of grades")
  void testComputeAverageGrade() throws Exception {
    Module module = new Module();
    List<Grade> grades = new ArrayList<>();

    Grade grade1 = new Grade();
    grade1.setScore(80);
    Grade grade2 = new Grade();
    grade2.setScore(100);

    grades.add(grade1);
    grades.add(grade2);

    float avg = module.computeAverageGrade(grades);

    assertEquals(90.0f, avg, "Average of 80 and 100 should be 90.0f");
  }

  /**
   * Tests that attempting to compute the average grade from an empty grade list
   * results in a NoGradeAvailableException.
   */
  @Test
  @DisplayName("Should throw NoGradeAvailableException if no grades are provided")
  void testComputeAverageGradeNoGrades() {
    Module module = new Module();
    assertThrows(NoGradeAvailableException.class, () -> module.computeAverageGrade(new ArrayList<>()),
        "Should throw NoGradeAvailableException when no grades are available");
  }
}
