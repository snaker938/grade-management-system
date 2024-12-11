package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Registration class.
 *
 * Registration represents the link between a Student and a Module.
 * This test verifies:
 * - Setting and retrieving the registration ID.
 * - Associating a Registration with a Student.
 * - Associating a Registration with a Module.
 */
public final class RegistrationTest {

  private Registration registration;
  private Student student;
  private Module module;

  /**
   * Sets up a new Registration, Student, and Module before each test.
   */
  @BeforeEach
  public void setUp() {
    registration = new Registration();

    student = new Student();
    student.setId(123L);
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setUsername("jdoe");
    student.setEmail("john.doe@example.com");

    module = new Module();
    module.setCode("MOD123");
    module.setName("Test Module");
    module.setMnc(true);
  }

  /**
   * Tests setting and getting the Registration ID.
   */
  @Test
  @DisplayName("Should set and get Registration ID correctly")
  public void testSetAndGetId() {
    registration.setId(200L);
    assertEquals(200L, registration.getId(), "ID should be 200");
  }

  /**
   * Tests associating a Registration with a Student.
   */
  @Test
  @DisplayName("Should associate Registration with a Student correctly")
  public void testAssociateStudent() {
    registration.setStudent(student);

    assertAll("Student should be associated correctly",
        () -> assertNotNull(registration.getStudent(), "Student reference should not be null"),
        () -> assertEquals(123L, registration.getStudent().getId(), "Student ID should be 123"),
        () -> assertEquals("John", registration.getStudent().getFirstName(), "Student first name should be John"),
        () -> assertEquals("Doe", registration.getStudent().getLastName(), "Student last name should be Doe"),
        () -> assertEquals("jdoe", registration.getStudent().getUsername(), "Student username should be jdoe"),
        () -> assertEquals("john.doe@example.com", registration.getStudent().getEmail(),
            "Student email should be john.doe@example.com"));
  }

  /**
   * Tests associating a Registration with a Module.
   */
  @Test
  @DisplayName("Should associate Registration with a Module correctly")
  public void testAssociateModule() {
    registration.setModule(module);

    assertAll("Module should be associated correctly",
        () -> assertNotNull(registration.getModule(), "Module reference should not be null"),
        () -> assertEquals("MOD123", registration.getModule().getCode(), "Module code should be MOD123"),
        () -> assertEquals("Test Module", registration.getModule().getName(), "Module name should be 'Test Module'"),
        () -> assertEquals(true, registration.getModule().isMnc(), "MNC should be true"));
  }
}
