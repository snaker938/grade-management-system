package uk.ac.ucl.comp0010.exception;

/**
 * Exception thrown when attempting to access grades for a module in which the
 * student is not registered.
 * This may occur, for example, if the student never enrolled in the specified
 * module or if the records do not reflect their registration status.
 */
public class NoRegistrationException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new NoRegistrationException with the specified detail message.
   *
   * @param message a detail message describing the reason for the exception
   */
  public NoRegistrationException(String message) {
    super(message);
  }

  /**
   * Constructs a new NoRegistrationException with the specified detail message
   * and cause.
   *
   * @param message a detail message describing the reason for the exception
   * @param cause   the cause of this exception
   */
  public NoRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
