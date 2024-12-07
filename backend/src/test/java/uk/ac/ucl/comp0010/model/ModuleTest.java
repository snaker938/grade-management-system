package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Module class.
 * Verifies that fields can be set and retrieved correctly,
 * default values are as expected, and toString() returns a meaningful
 * representation.
 */
class ModuleTest {

  private Module module;

  /**
   * Creates a new Module instance before each test.
   */
  @BeforeEach
  void setUp() {
    module = new Module();
  }

  /**
   * Tests that all fields of a Module can be set and retrieved correctly.
   */
  @Test
  @DisplayName("Should set and get all Module fields correctly")
  void testModuleFields() {
    module.setCode("COM0010");
    module.setName("Software Engineering");
    module.setMnc(true);

    assertAll("All fields should match the values that were set",
        () -> assertEquals("COM0010", module.getCode(), "Code should be COM0010"),
        () -> assertEquals("Software Engineering", module.getName(), "Name should be Software Engineering"),
        () -> assertEquals(true, module.isMnc(), "MNC should be true"));
  }

  /**
   * Tests that a newly instantiated Module object has default values of null (or
   * default) for all fields.
   * Adjust if the Module class uses different defaults.
   */
  @Test
  @DisplayName("Should have default values on a new Module instance")
  void testDefaultValues() {
    assertAll("All fields should be null or default by default",
        () -> assertEquals(null, module.getCode(), "Default code should be null"),
        () -> assertEquals(null, module.getName(), "Default name should be null"),
        () -> assertEquals(false, module.isMnc(), "Default MNC should be false"));
  }

  /**
   * Tests that toString() returns a non-null string containing all field values.
   */
  @Test
  @DisplayName("toString() should return a non-null string containing all field values")
  void testToString() {
    module.setCode("COM0010");
    module.setName("Software Engineering");
    module.setMnc(true);

    String moduleString = module.toString();
    assertNotNull(moduleString, "toString() should never return null");
    assertAll("String representation should contain all field values",
        () -> assertEquals(true, moduleString.contains("COM0010"), "Should contain the code"),
        () -> assertEquals(true, moduleString.contains("Software Engineering"), "Should contain the name"),
        () -> assertEquals(true, moduleString.contains("true"), "Should contain the MNC value"));
  }
}
