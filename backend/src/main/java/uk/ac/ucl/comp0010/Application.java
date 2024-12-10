package uk.ac.ucl.comp0010;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * Starts the embedded server and initializes all components.
 */
@SpringBootApplication
public class Application {

  /**
   * Launches the application.
   * 
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
