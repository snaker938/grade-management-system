package uk.ac.ucl.comp0010.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;  
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;  
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Integration tests for controller endpoints, verifying that endpoints
 * for grades, modules, and students work as intended.
 *
 * This includes:
 * - Adding, updating, and retrieving grades via GradeController.
 * - Registering students to modules, removing students, and retrieving
 *   module registrations via ModuleController.
 * - Updating student information via StudentController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public final class ControllerIntegrationTest {

  /** Provides the ability to perform MVC requests in tests. */
  @Autowired
  private MockMvc mockMvc;

  /** Used to serialize/deserialize objects to/from JSON. */
  @Autowired
  private ObjectMapper objectMapper;

  /** Repository for managing Student entities. */
  @Autowired
  private StudentRepository studentRepository;

  /** Repository for managing Module entities. */
  @Autowired
  private ModuleRepository moduleRepository;

  /** Repository for managing Grade entities. */
  @Autowired
  private GradeRepository gradeRepository;

  /** Repository for managing Registration entities. */
  @Autowired
  private RegistrationRepository registrationRepository;

  private Student student;
  private Module module;
  private Grade grade;

  /**
   * Sets up test data by clearing existing records and creating a default
   * student, module, and grade scenario before each test.
   */
  @BeforeEach
  public void setUp() {
    registrationRepository.deleteAll();
    gradeRepository.deleteAll();
    studentRepository.deleteAll();
    moduleRepository.deleteAll();

    student = new Student();
    student.setId(1L);
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setUsername("jdoe");
    student.setEmail("jdoe@example.com");
    studentRepository.save(student);

    module = new Module();
    module.setCode("MOD001");
    module.setName("Software Engineering");
    module.setMnc(true);
    module.setMaxSeats(2);
    moduleRepository.save(module);

    // Register the student in the module
    student.registerModule(module);
    // Save the registration
    registrationRepository.saveAll(student.getRegistrations());

    grade = new Grade();
    grade.setScore(90);
    grade.setAcademicYear("2024/2025");
    grade.setStudent(student);
    grade.setModule(module);
    gradeRepository.save(grade);
  }

  /**
   * Tests adding a grade successfully via GradeController.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController addGrade endpoint - success")
  public void testAddGradeSuccess() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("student_id", "1");
    params.put("module_code", "MOD001");
    params.put("score", "85");
    params.put("academic_year", "2025/2026");

    mockMvc.perform(post("/grades/addGrade")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params)))
        .andExpect(status().isOk());
  }

  /**
   * Tests adding a grade with missing parameters.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController addGrade endpoint - missing params")
  public void testAddGradeMissingParams() throws Exception {
    mockMvc.perform(post("/grades/addGrade")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.emptyMap())))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests adding a grade when the specified student is not found.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController addGrade endpoint - student not found")
  public void testAddGradeStudentNotFound() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("student_id", "999");
    params.put("module_code", "MOD001");
    params.put("score", "50");
    params.put("academic_year", "2025/2026");

    mockMvc.perform(post("/grades/addGrade")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params)))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests adding a grade when the specified module is not found.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController addGrade endpoint - module not found")
  public void testAddGradeModuleNotFound() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("student_id", "1");
    params.put("module_code", "XYZ999");
    params.put("score", "50");
    params.put("academic_year", "2025/2026");

    mockMvc.perform(post("/grades/addGrade")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params)))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests adding a grade when the student is not enrolled in the module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController addGrade endpoint - student not enrolled")
  public void testAddGradeStudentNotEnrolled() throws Exception {
    Module otherModule = new Module();
    otherModule.setCode("MOD002");
    otherModule.setName("Data Structures");
    otherModule.setMnc(false);
    otherModule.setMaxSeats(1);
    moduleRepository.save(otherModule);

    Map<String, String> params = new HashMap<>();
    params.put("student_id", "1");
    params.put("module_code", "MOD002");
    params.put("score", "75");
    params.put("academic_year", "2025/2026");

    mockMvc.perform(post("/grades/addGrade")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params)))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests updating an existing grade successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController updateGrade endpoint - success")
  public void testUpdateGradeSuccess() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("score", "95");
    params.put("academic_year", "2026/2027");

    mockMvc.perform(put("/grades/" + grade.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params)))
        .andExpect(status().isOk());
  }

  /**
   * Tests updating a non-existent grade.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test GradeController updateGrade endpoint - not found")
  public void testUpdateGradeNotFound() throws Exception {
    mockMvc.perform(put("/grades/9999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.emptyMap())))
        .andExpect(status().isNotFound());
  }

  // ModuleController tests

  /**
   * Tests registering a student in a module successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - success")
  public void testRegisterStudentSuccess() throws Exception {
    Student s2 = new Student();
    s2.setId(2L);
    s2.setFirstName("Jane");
    s2.setLastName("Smith");
    s2.setUsername("jsmith");
    s2.setEmail("jsmith@example.com");
    studentRepository.save(s2);

    Map<String,String> body = new HashMap<>();
    body.put("studentId", "2");
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk());
  }

  /**
   * Tests registering a student without providing a studentId.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - missing studentId")
  public void testRegisterStudentMissingParam() throws Exception {
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.emptyMap())))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests registering a student for a non-existent module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - module not found")
  public void testRegisterStudentModuleNotFound() throws Exception {
    Map<String,String> body = new HashMap<>();
    body.put("studentId", "1");
    mockMvc.perform(post("/modules/XYZ999/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests registering a non-existent student to a module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - student not found")
  public void testRegisterStudentStudentNotFound() throws Exception {
    Map<String,String> body = new HashMap<>();
    body.put("studentId", "9999");
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests registering a student to a module that is already full.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - module full")
  public void testRegisterStudentModuleFull() throws Exception {
    // Enroll another student to fill the module
    Student s3 = new Student();
    s3.setId(3L);
    s3.setFirstName("Alice");
    s3.setLastName("Green");
    s3.setUsername("agreen");
    s3.setEmail("agreen@example.com");
    studentRepository.save(s3);

    Map<String,String> body = new HashMap<>();
    body.put("studentId", "3");
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk());

    // Now module is full (maxSeats=2)
    Student s4 = new Student();
    s4.setId(4L);
    s4.setFirstName("Bob");
    s4.setLastName("Brown");
    s4.setUsername("bbrown");
    s4.setEmail("bbrown@example.com");
    studentRepository.save(s4);

    body.put("studentId", "4");
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests registering a student who is already registered in the module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test registerStudent endpoint - already registered")
  public void testRegisterStudentAlreadyRegistered() throws Exception {
    // Student 1 is already registered from setUp()
    Map<String,String> body = new HashMap<>();
    body.put("studentId", "1");
    mockMvc.perform(post("/modules/MOD001/registerStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests removing a student from a module successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test removeStudent endpoint - success")
  public void testRemoveStudentSuccess() throws Exception {
    // Student 1 is registered in MOD001
    mockMvc.perform(delete("/modules/MOD001/students/1"))
        .andExpect(status().isOk());
  }

  /**
   * Tests removing a student from a non-existent module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test removeStudent endpoint - module not found")
  public void testRemoveStudentModuleNotFound() throws Exception {
    mockMvc.perform(delete("/modules/XYZ999/students/1"))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests removing a non-existent student from a module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test removeStudent endpoint - student not found")
  public void testRemoveStudentStudentNotFound() throws Exception {
    mockMvc.perform(delete("/modules/MOD001/students/9999"))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests removing a student who is not registered in the module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test removeStudent endpoint - student not registered")
  public void testRemoveStudentNotRegistered() throws Exception {
    Module mod2 = new Module();
    mod2.setCode("MODX");
    mod2.setName("Physics");
    mod2.setMnc(false);
    mod2.setMaxSeats(10);
    moduleRepository.save(mod2);

    mockMvc.perform(delete("/modules/MODX/students/1"))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests retrieving module registrations successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test getModuleRegistrations endpoint - success")
  public void testGetModuleRegistrationsSuccess() throws Exception {
    mockMvc.perform(get("/modules/MOD001/registrations"))
        .andExpect(status().isOk());
  }

  /**
   * Tests retrieving registrations for a non-existent module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test getModuleRegistrations endpoint - module not found")
  public void testGetModuleRegistrationsNotFound() throws Exception {
    mockMvc.perform(get("/modules/XYZ999/registrations"))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests updating a module successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateModule endpoint - success")
  public void testUpdateModuleSuccess() throws Exception {
    Map<String,String> body = new HashMap<>();
    body.put("name", "Updated Name");
    body.put("mnc", "false");
    body.put("maxSeats", "5");

    mockMvc.perform(put("/modules/MOD001")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk());
  }

  /**
   * Tests updating a non-existent module.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateModule endpoint - module not found")
  public void testUpdateModuleNotFound() throws Exception {
    mockMvc.perform(put("/modules/XYZ999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.emptyMap())))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests updating a module with invalid maxSeats value (negative),
   * ensuring it's either not updated or handled gracefully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateModule endpoint - invalid maxSeats")
  public void testUpdateModuleInvalidMaxSeats() throws Exception {
    Map<String,String> body = new HashMap<>();
    body.put("maxSeats", "-1"); // won't be updated

    mockMvc.perform(put("/modules/MOD001")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk());
  }

  // StudentController tests

  /**
   * Tests updating a student's fields successfully.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateStudent endpoint - success")
  public void testUpdateStudentSuccess() throws Exception {
    Student updated = new Student();
    updated.setFirstName("Johnny");
    updated.setLastName("Doh");
    updated.setUsername("jdoh");
    updated.setEmail("jdoh@example.com");

    mockMvc.perform(put("/students/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk());
  }

  /**
   * Tests updating a non-existent student.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateStudent endpoint - not found")
  public void testUpdateStudentNotFound() throws Exception {
    Student updated = new Student();
    updated.setFirstName("NoOne");

    mockMvc.perform(put("/students/9999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests partially updating a student's fields.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateStudent endpoint - partial fields")
  public void testUpdateStudentPartialFields() throws Exception {
    Student updated = new Student();
    updated.setEmail("newemail@example.com");
    // no other fields

    mockMvc.perform(put("/students/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk());
  }

  /**
   * Tests updating a student with an empty body, which should still return OK
   * and not update fields.
   *
   * @throws Exception if an MVC request fails
   */
  @Test
  @DisplayName("Test updateStudent endpoint - empty body")
  public void testUpdateStudentEmptyBody() throws Exception {
    mockMvc.perform(put("/students/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new Student())))
        .andExpect(status().isOk());
  }
}
