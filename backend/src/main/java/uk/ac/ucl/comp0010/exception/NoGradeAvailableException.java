package uk.ac.ucl.comp0010.exception;

/**
 * Exception thrown to indicate that no grade is available, either at all or for
 * a specific module.
 * This may occur if a student has not been graded yet, if the requested module
 * does not have a recorded grade, or if the data source has no grade
 * information for the given context.
 */
public class NoGradeAvailableException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new NoGradeAvailableException with the specified detail message.
   *
   * @param message a detail message describing the reason for the exception
   */
  public NoGradeAvailableException(String message) {
    super(message);
  }

  /**
   * Constructs a new NoGradeAvailableException with the specified detail message
   * and cause.
   *
   * @param message a detail message describing the reason for the exception
   * @param cause   the cause of this exception
   */
  public NoGradeAvailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
