package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Student class.
 * Verifies that fields can be set and retrieved correctly,
 * default values are as expected, and toString() returns a meaningful
 * representation.
 */
class StudentTest {

  private Student student;

  /**
   * Creates a new Student instance before each test.
   */
  @BeforeEach
  void setUp() {
    student = new Student();
  }

  /**
   * Tests that all fields of a Student can be set and retrieved correctly.
   */
  @Test
  @DisplayName("Should set and get all Student fields correctly")
  void testStudentFields() {
    student.setId(1L);
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setUsername("jdoe");
    student.setEmail("john.doe@example.com");

    assertAll("All fields should match the values that were set",
        () -> assertEquals(1L, student.getId(), "ID should be 1"),
        () -> assertEquals("John", student.getFirstName(), "First name should be John"),
        () -> assertEquals("Doe", student.getLastName(), "Last name should be Doe"),
        () -> assertEquals("jdoe", student.getUsername(), "Username should be jdoe"),
        () -> assertEquals("john.doe@example.com", student.getEmail(), "Email should be john.doe@example.com"));
  }

  /**
   * Tests that a newly instantiated Student object has default values of null for
   * all fields.
   * Adjust if the Student class uses different defaults.
   */
  @Test
  @DisplayName("Should have default values on a new Student instance")
  void testDefaultValues() {
    assertAll("All fields should be null by default",
        () -> assertEquals(null, student.getId(), "Default ID should be null"),
        () -> assertEquals(null, student.getFirstName(), "Default firstName should be null"),
        () -> assertEquals(null, student.getLastName(), "Default lastName should be null"),
        () -> assertEquals(null, student.getUsername(), "Default username should be null"),
        () -> assertEquals(null, student.getEmail(), "Default email should be null"));
  }

  /**
   * Tests that toString() returns a non-null string containing all field values.
   */
  @Test
  @DisplayName("toString() should return a non-null string containing all field values")
  void testToString() {
    student.setId(1L);
    student.setFirstName("Jane");
    student.setLastName("Smith");
    student.setUsername("jsmith");
    student.setEmail("jane.smith@example.com");

    String studentString = student.toString();
    assertNotNull(studentString, "toString() should never return null");
    assertAll("String representation should contain all field values",
        () -> assertEquals(true, studentString.contains("Jane"), "Should contain the first name"),
        () -> assertEquals(true, studentString.contains("Smith"), "Should contain the last name"),
        () -> assertEquals(true, studentString.contains("jsmith"), "Should contain the username"),
        () -> assertEquals(true, studentString.contains("jane.smith@example.com"), "Should contain the email"),
        () -> assertEquals(true, studentString.contains("1"), "Should contain the ID"));
  }
}
