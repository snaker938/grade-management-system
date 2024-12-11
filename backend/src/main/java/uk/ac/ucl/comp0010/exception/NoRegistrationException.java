package uk.ac.ucl.comp0010.exception;

/**
 * Exception thrown when attempting to access grades for a module in which
 * the student is not registered. This may occur if the student never enrolled
 * in the specified module or if the records do not reflect their registration.
 */
public class NoRegistrationException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a NoRegistrationException with the given message.
   *
   * @param message a detail message describing the reason
   */
  public NoRegistrationException(final String message) {
    super(message);
  }

  /**
   * Constructs a NoRegistrationException with the given message and cause.
   *
   * @param message a detail message describing the reason
   * @param cause   the cause of this exception
   */
  public NoRegistrationException(final String message,
      final Throwable cause) {
    super(message, cause);
  }
}
