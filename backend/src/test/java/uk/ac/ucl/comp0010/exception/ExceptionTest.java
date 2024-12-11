package uk.ac.ucl.comp0010.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for custom exceptions.
 * Verifies that exception messages and causes are set and retrieved correctly.
 */
public final class ExceptionTest {

  /**
   * Tests that NoGradeAvailableException properly initializes with the given message.
   */
  @Test
  @DisplayName("NoGradeAvailableException single-arg constructor test")
  public void testNoGradeAvailableExceptionSingleArg() {
    NoGradeAvailableException ex = new NoGradeAvailableException("No grade");
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("No grade", ex.getMessage(),
        "Exception message should be 'No grade'");
  }

  /**
   * Tests the NoGradeAvailableException two-arg constructor,
   * ensuring the message and cause are correctly set.
   */
  @Test
  @DisplayName("NoGradeAvailableException two-arg constructor test")
  public void testNoGradeAvailableExceptionTwoArg() {
    Throwable cause = new Throwable("Cause");
    NoGradeAvailableException ex =
        new NoGradeAvailableException("No grade here", cause);
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("No grade here", ex.getMessage(),
        "Exception message should be 'No grade here'");
    assertEquals(cause, ex.getCause(), "Cause should match the one provided");
  }

  /**
   * Tests that NoRegistrationException properly initializes with the given message.
   */
  @Test
  @DisplayName("NoRegistrationException single-arg constructor test")
  public void testNoRegistrationExceptionSingleArg() {
    NoRegistrationException ex = new NoRegistrationException("Not registered");
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("Not registered", ex.getMessage(),
        "Exception message should be 'Not registered'");
  }

  /**
   * Tests the NoRegistrationException two-arg constructor,
   * ensuring the message and cause are correctly set.
   */
  @Test
  @DisplayName("NoRegistrationException two-arg constructor test")
  public void testNoRegistrationExceptionTwoArg() {
    Throwable cause = new Throwable("Cause");
    NoRegistrationException ex =
        new NoRegistrationException("Not registered here", cause);
    assertNotNull(ex, "Exception instance should not be null");
    assertEquals("Not registered here", ex.getMessage(),
        "Exception message should be 'Not registered here'");
    assertEquals(cause, ex.getCause(), "Cause should match the one provided");
  }
}
