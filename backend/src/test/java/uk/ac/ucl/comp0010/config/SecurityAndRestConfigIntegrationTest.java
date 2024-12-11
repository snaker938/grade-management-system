package uk.ac.ucl.comp0010.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Integration tests verifying that security and REST configurations work as intended.
 *
 * Ensures that:
 * - CORS settings allow requests from external origins.
 * - Entity IDs are exposed for repository endpoints.
 * - Basic endpoint access is not restricted by security.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public final class SecurityAndRestConfigIntegrationTest {

  /** Provides the ability to perform MVC requests in tests. */
  @Autowired
  private MockMvc mockMvc;

  /** Repository for managing Student entities. */
  @Autowired
  private StudentRepository studentRepository;

  /**
   * Ensures a clean state before each test by clearing repositories if necessary.
   */
  @BeforeEach
  public void setUp() {
    studentRepository.deleteAll();
  }

  /**
   * Verifies that the /students endpoint is accessible, that CORS headers are
   * present when an Origin header is provided, and that entity IDs are exposed.
   *
   * This test:
   * - Creates and saves a Student to ensure data is present.
   * - Performs a GET request with an Origin header.
   * - Checks that the response is OK, that CORS headers are included,
   *   and that the student ID is exposed in the JSON response.
   *
   * @throws Exception if an MVC request or expectation fails
   */
  @Test
  @DisplayName("Verify Security and REST Config for /students")
  public void testStudentsEndpoint() throws Exception {
    // Create and save a student to ensure data exists
    Student s = new Student();
    s.setId(100L);
    s.setFirstName("Test");
    s.setLastName("User");
    s.setUsername("tuser");
    s.setEmail("test.user@example.com");
    studentRepository.save(s);

    // Perform a GET request with an Origin header to trigger CORS logic
    mockMvc.perform(get("/students").header("Origin", "http://localhost:3000"))
        .andExpect(status().isOk())
        .andExpect(header().exists("Access-Control-Allow-Origin"))
        .andExpect(jsonPath("$._embedded.students").exists())
        .andExpect(jsonPath("$._embedded.students[0].id").exists());
  }
}
