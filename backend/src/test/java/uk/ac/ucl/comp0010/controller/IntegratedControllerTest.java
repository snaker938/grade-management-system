package uk.ac.ucl.comp0010.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import uk.ac.ucl.comp0010.model.*;
import uk.ac.ucl.comp0010.repository.*;

class IntegratedTest {

  private StudentRepository mockStudentRepo;
  private ModuleRepository mockModuleRepo;
  private GradeRepository mockGradeRepo;
  private RegistrationRepository mockRegRepo;

  private GradeController gradeController;
  private ModuleController moduleController;
  private StudentController studentController;

  private Student testStudent;
  private uk.ac.ucl.comp0010.model.Module testModule;
  private Grade testGrade;
  private Registration testRegistration;

  @BeforeEach
  void setup() {
    mockStudentRepo = Mockito.mock(StudentRepository.class);
    mockModuleRepo = Mockito.mock(ModuleRepository.class);
    mockGradeRepo = Mockito.mock(GradeRepository.class);
    mockRegRepo = Mockito.mock(RegistrationRepository.class);

    gradeController = new GradeController(mockStudentRepo, mockModuleRepo, mockGradeRepo);
    moduleController = new ModuleController(mockModuleRepo, mockStudentRepo, mockRegRepo, mockGradeRepo);
    studentController = new StudentController(mockStudentRepo);

    // Setup test data
    testStudent = new Student();
    testStudent.setId(1L);
    testStudent.setFirstName("Alice");
    testStudent.setLastName("Smith");
    testStudent.setUsername("asmith");
    testStudent.setEmail("alice@example.com");

    testModule = new uk.ac.ucl.comp0010.model.Module();
    testModule.setCode("MODX");
    testModule.setName("Test Module");
    testModule.setMnc(false);

    testGrade = new Grade();
    testGrade.setId(100L);
    testGrade.setScore(90);
    testGrade.setAcademicYear("2024/2025");
    testGrade.setStudent(testStudent);
    testGrade.setModule(testModule);

    testRegistration = new Registration();
    testRegistration.setId(200L);
    testRegistration.setStudent(testStudent);
    testRegistration.setModule(testModule);

    // Mock basic repository returns
    when(mockStudentRepo.findById(1L)).thenReturn(Optional.of(testStudent));
    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));
  }

  @Test
  @DisplayName("Add Grade - Student Not Registered")
  void testAddGradeStudentNotRegistered() {
    // No registrations in module means not registered
    testModule.setRegistrations(new ArrayList<>());

    Map<String, String> params = new HashMap<>();
    params.put("student_id", "1");
    params.put("module_code", "MODX");
    params.put("score", "95");
    params.put("academic_year", "2024/2025");

    ResponseEntity<Grade> response = gradeController.addGrade(params);
    assertEquals(400, response.getStatusCode().value()); // BAD_REQUEST
  }

  @Test
  @DisplayName("Update Existing Grade")
  void testUpdateGrade() {
    when(mockGradeRepo.findById(100L)).thenReturn(Optional.of(testGrade));
    Map<String, String> params = new HashMap<>();
    params.put("score", "100");
    params.put("academic_year", "2025/2026");

    when(mockGradeRepo.save(any(Grade.class))).thenAnswer(i -> i.getArgument(0));

    ResponseEntity<Grade> response = gradeController.updateGrade(100L, params);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(100, response.getBody().getScore());
    assertEquals("2025/2026", response.getBody().getAcademicYear());
  }

  @Test
  @DisplayName("Update Non-Existent Grade")
  void testUpdateNonExistentGrade() {
    when(mockGradeRepo.findById(999L)).thenReturn(Optional.empty());

    ResponseEntity<Grade> response = gradeController.updateGrade(999L, new HashMap<>());
    assertEquals(404, response.getStatusCode().value()); // NOT_FOUND
  }

  @Test
  @DisplayName("Register Student to Module")
  void testRegisterStudentToModule() {
    testModule.setMaxSeats(2);
    testModule.setRegistrations(new ArrayList<>());

    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));

    Map<String, String> body = new HashMap<>();
    body.put("studentId", "1");

    when(mockRegRepo.save(any(Registration.class))).thenAnswer(i -> i.getArgument(0));

    ResponseEntity<?> response = moduleController.registerStudent("MODX", body);
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @DisplayName("Register Student - Module Full")
  void testRegisterStudentModuleFull() {
    testModule.setMaxSeats(1);
    testModule.setRegistrations(Collections.singletonList(testRegistration));

    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));

    Map<String, String> body = new HashMap<>();
    body.put("studentId", "1");

    ResponseEntity<?> response = moduleController.registerStudent("MODX", body);
    assertEquals(400, response.getStatusCode().value()); // BAD_REQUEST, capacity reached
  }

  @Test
  @DisplayName("Remove Student from Module")
  void testRemoveStudentFromModule() {
    testModule.setRegistrations(Collections.singletonList(testRegistration));
    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));
    when(mockRegRepo.findAll()).thenReturn(Collections.singletonList(testRegistration));

    // Removing student
    ResponseEntity<?> response = moduleController.removeStudent("MODX", 1L);
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @DisplayName("Remove Student not in Module")
  void testRemoveStudentNotInModule() {
    testModule.setRegistrations(new ArrayList<>());
    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));

    ResponseEntity<?> response = moduleController.removeStudent("MODX", 1L);
    assertEquals(400, response.getStatusCode().value()); // Not registered
  }

  @Test
  @DisplayName("Get Module Registrations")
  void testGetModuleRegistrations() {
    // Setup module with one registration and grade
    testModule.setRegistrations(Collections.singletonList(testRegistration));
    testModule.setGrades(Collections.singletonList(testGrade));
    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));

    ResponseEntity<?> response = moduleController.getModuleRegistrations("MODX");
    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() instanceof Map);

    Map<?, ?> result = (Map<?, ?>) response.getBody();
    List<?> enrolled = (List<?>) result.get("enrolledStudents");
    assertEquals(1, enrolled.size());
  }

  @Test
  @DisplayName("Edit Student")
  void testEditStudent() {
    when(mockStudentRepo.findById(1L)).thenReturn(Optional.of(testStudent));

    Student updated = new Student();
    updated.setEmail("newemail@example.com");

    when(mockStudentRepo.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

    ResponseEntity<Student> resp = studentController.updateStudent(1L, updated);
    assertEquals(200, resp.getStatusCode().value());
    assertEquals("newemail@example.com", resp.getBody().getEmail());
  }

  @Test
  @DisplayName("Edit Non-existent Student")
  void testEditNonExistentStudent() {
    when(mockStudentRepo.findById(999L)).thenReturn(Optional.empty());
    ResponseEntity<Student> resp = studentController.updateStudent(999L, new Student());
    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  @DisplayName("Update Module details")
  void testUpdateModule() {
    when(mockModuleRepo.findById("MODX")).thenReturn(Optional.of(testModule));
    Map<String, String> body = new HashMap<>();
    body.put("name", "New Module Name");
    body.put("mnc", "true");
    body.put("maxSeats", "50");

    when(mockModuleRepo.save(any(uk.ac.ucl.comp0010.model.Module.class))).thenAnswer(i -> i.getArgument(0));

    ResponseEntity<uk.ac.ucl.comp0010.model.Module> resp = moduleController.updateModule("MODX", body);
    assertEquals(200, resp.getStatusCode().value());
    assertEquals("New Module Name", resp.getBody().getName());
    assertTrue(resp.getBody().isMnc());
    assertEquals(50, resp.getBody().getMaxSeats());
  }

  @Test
  @DisplayName("Update Non-existent Module")
  void testUpdateNonExistentModule() {
    when(mockModuleRepo.findById("NOMOD")).thenReturn(Optional.empty());
    ResponseEntity<uk.ac.ucl.comp0010.model.Module> resp = moduleController.updateModule("NOMOD",
        Collections.emptyMap());
    assertEquals(404, resp.getStatusCode().value());
  }
}
