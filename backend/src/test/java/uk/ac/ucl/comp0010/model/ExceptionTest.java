package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;

/**
 * Unit tests for custom exceptions.
 * Verifies that exception messages are set and retrieved correctly.
 */
class ExceptionTest {

  /**
   * Tests that NoGradeAvailableException properly initializes with the given
   * message.
   */
  @Test
  @DisplayName("NoGradeAvailableException should return the correct message")
  void testNoGradeAvailableException() {
    NoGradeAvailableException ex = new NoGradeAvailableException("No grade");
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("No grade", ex.getMessage(), "Exception message should be 'No grade'");
  }

  /**
   * Tests that NoRegistrationException properly initializes with the given
   * message.
   */
  @Test
  @DisplayName("NoRegistrationException should return the correct message")
  void testNoRegistrationException() {
    NoRegistrationException ex = new NoRegistrationException("Not registered");
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("Not registered", ex.getMessage(), "Exception message should be 'Not registered'");
  }
}
