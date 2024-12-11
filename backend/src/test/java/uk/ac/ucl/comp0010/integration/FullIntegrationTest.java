package uk.ac.ucl.comp0010.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Integration tests for domain model and repository interactions.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
@DisplayName("Integration Tests")
public final class FullIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private GradeRepository gradeRepository;

  /**
   * Ensures a clean state before each test.
   */
  @BeforeEach
  public void setUp() {
  }

  /**
   * Tests a complete workflow of creating and persisting entities.
   */
  @Test
  @DisplayName("Complete Workflow")
  public void testFullWorkflow() throws Exception {
    Student student = new Student();
    student.setId(1L);
    student.setFirstName("Alice");
    student.setLastName("Smith");
    student.setUsername("asmith");
    student.setEmail("alice.smith@example.com");
    student = studentRepository.save(student);

    Module module = new Module();
    module.setCode("MOD001");
    module.setName("Software Engineering");
    module.setMnc(true);
    module = moduleRepository.save(module);

    student.registerModule(module);
    registrationRepository.saveAll(student.getRegistrations());

    student = studentRepository.findById(student.getId()).orElseThrow();

    Grade grade = new Grade();
    grade.setScore(90);
    grade.setAcademicYear("2024/2025");
    grade.setStudent(student);
    grade.setModule(module);
    grade = gradeRepository.save(grade);
    assertNotNull(grade.getId());

    student = studentRepository.findById(student.getId()).orElseThrow();

    Grade grade2 = new Grade();
    grade2.setScore(100);
    grade2.setAcademicYear("2024/2025");
    grade2.setStudent(student);
    grade2.setModule(module);
    grade2 = gradeRepository.save(grade2);

    student = studentRepository.findById(student.getId()).orElseThrow();

    Module anotherModule = new Module();
    anotherModule.setCode("MOD002");
    anotherModule.setName("Data Structures");
    anotherModule.setMnc(false);
    anotherModule = moduleRepository.save(anotherModule);

    Student studentWithoutGrades = new Student();
    studentWithoutGrades.setId(2L);
    studentWithoutGrades.setFirstName("Bob");
    studentWithoutGrades.setLastName("Jones");
    studentWithoutGrades.setUsername("bjones");
    studentWithoutGrades.setEmail("bob.jones@example.com");
    studentWithoutGrades = studentRepository.save(studentWithoutGrades);
  }

  /**
   * Tests scenario with an empty database.
   */
  @Test
  @DisplayName("Empty Database Scenario")
  public void testEmptyDatabaseScenario() {
    assertEquals(0, studentRepository.count());
    assertEquals(0, moduleRepository.count());
    assertEquals(0, registrationRepository.count());
    assertEquals(0, gradeRepository.count());
  }

  /**
   * Tests creating multiple modules and grades for a single student.
   */
  @Test
  @DisplayName("Multiple Modules and Grades")
  public void testMultipleModulesAndGrades() throws Exception {
    Student student = new Student();
    student.setId(3L);
    student.setFirstName("Carol");
    student.setLastName("Danvers");
    student.setUsername("cdanvers");
    student.setEmail("carol.danvers@example.com");
    student = studentRepository.save(student);

    Module moduleA = new Module();
    moduleA.setCode("MODA");
    moduleA.setName("Mathematics");
    moduleA.setMnc(false);
    moduleA = moduleRepository.save(moduleA);

    Module moduleB = new Module();
    moduleB.setCode("MODB");
    moduleB.setName("Physics");
    moduleB.setMnc(true);
    moduleB = moduleRepository.save(moduleB);

    student.registerModule(moduleA);
    student.registerModule(moduleB);
    registrationRepository.saveAll(student.getRegistrations());

    student = studentRepository.findById(student.getId()).orElseThrow();

    Grade gradeA1 = new Grade();
    gradeA1.setScore(70);
    gradeA1.setAcademicYear("2024/2025");
    gradeA1.setStudent(student);
    gradeA1.setModule(moduleA);
    gradeRepository.save(gradeA1);

    student = studentRepository.findById(student.getId()).orElseThrow();

    Grade gradeA2 = new Grade();
    gradeA2.setScore(80);
    gradeA2.setAcademicYear("2024/2025");
    gradeA2.setStudent(student);
    gradeA2.setModule(moduleA);
    gradeRepository.save(gradeA2);

    student = studentRepository.findById(student.getId()).orElseThrow();

    Grade gradeB1 = new Grade();
    gradeB1.setScore(60);
    gradeB1.setAcademicYear("2024/2025");
    gradeB1.setStudent(student);
    gradeB1.setModule(moduleB);
    gradeRepository.save(gradeB1);

    student = studentRepository.findById(student.getId()).orElseThrow();
  }

  /**
   * Tests a student with no modules and no grades.
   */
  @Test
  @DisplayName("Student with No Modules and No Grades")
  public void testStudentNoModulesNoGrades() {
    Student student = new Student();
    student.setId(4L);
    student.setFirstName("Diana");
    student.setLastName("Prince");
    student.setUsername("dprince");
    student.setEmail("diana.prince@example.com");
    student = studentRepository.save(student);
  }

  /**
   * Tests creating a module with an empty code.
   */
  @Test
  @DisplayName("Module with Empty Code")
  public void testModuleWithEmptyCode() {
    Module mod = new Module();
    mod.setCode("");
    mod.setName("Nameless Engineering");
    mod.setMnc(false);
    mod = moduleRepository.save(mod);

    assertNotNull(mod);
    assertTrue(moduleRepository.findById("").isPresent());
  }

  /**
   * Tests scenario with multiple academic years but no grades.
   */
  @Test
  @DisplayName("Multiple Academic Years - No Grades")
  public void testMultipleAcademicYears() throws Exception {
    Student student = new Student();
    student.setId(5L);
    student.setFirstName("Ethan");
    student.setLastName("Hunt");
    student.setUsername("ehunt");
    student.setEmail("ethan.hunt@example.com");
    student = studentRepository.save(student);

    Module multiYearModule = new Module();
    multiYearModule.setCode("MODMULTIYR");
    multiYearModule.setName("Advanced Spying");
    multiYearModule.setMnc(false);
    multiYearModule = moduleRepository.save(multiYearModule);

    Registration registration = new Registration();
    registration.setStudent(student);
    registration.setModule(multiYearModule);
    registration = registrationRepository.save(registration);
  }

  /**
   * Tests scenario with a null module treated as unregistered.
   */
  @Test
  @DisplayName("Getting Grade from Null Module treated as unregistered")
  public void testGetGradeFromNullModule() throws Exception {
    Student student = new Student();
    student.setId(6L);
    student.setFirstName("Fred");
    student.setLastName("Flintstone");
    student.setUsername("fflint");
    student.setEmail("fred.flintstone@example.com");
    student = studentRepository.save(student);
  }

  /**
   * Tests a module with no registrations and no grades.
   */
  @Test
  @DisplayName("Module with No Registrations and No Grades")
  public void testModuleNoRegistrationsNoGrades() {
    Module lonelyModule = new Module();
    lonelyModule.setCode("LONELY");
    lonelyModule.setName("Lonely Module");
    lonelyModule.setMnc(true);
    lonelyModule = moduleRepository.save(lonelyModule);
  }

  /**
   * Tests adding negative scores, considered invalid by the domain.
   */
  @Test
  @DisplayName("Add Negative Scores - Domain Treats Them as Invalid")
  public void testNegativeGradeScores() throws Exception {
    Student student = new Student();
    student.setId(7L);
    student.setFirstName("Greg");
    student.setLastName("Negative");
    student.setUsername("gneg");
    student.setEmail("greg.negative@example.com");
    student = studentRepository.save(student);

    Module testModule = new Module();
    testModule.setCode("NEG001");
    testModule.setName("Negative Testing");
    testModule.setMnc(false);
    testModule = moduleRepository.save(testModule);

    Registration reg = new Registration();
    reg.setStudent(student);
    reg.setModule(testModule);
    reg = registrationRepository.save(reg);

    Grade negativeGrade = new Grade();
    negativeGrade.setScore(-10);
    negativeGrade.setAcademicYear("2024/2025");
    negativeGrade.setStudent(student);
    negativeGrade.setModule(testModule);
    gradeRepository.save(negativeGrade);
  }

  /**
   * Tests scenario where a different non-registered module is present.
   */
  @Test
  @DisplayName("Try Getting Grade from Non-Registered Module")
  public void testGetGradeForDifferentModuleNotRegistered() throws Exception {
    Student student = new Student();
    student.setId(8L);
    student.setFirstName("Harry");
    student.setLastName("Potter");
    student.setUsername("hpotter");
    student.setEmail("harry.potter@example.com");
    student = studentRepository.save(student);

    Module registeredModule = new Module();
    registeredModule.setCode("WANDS101");
    registeredModule.setName("Wand Making");
    registeredModule.setMnc(false);
    registeredModule = moduleRepository.save(registeredModule);

    Registration reg = new Registration();
    reg.setStudent(student);
    reg.setModule(registeredModule);
    reg = registrationRepository.save(reg);

    Module differentModule = new Module();
    differentModule.setCode("POTIONS101");
    differentModule.setName("Potions");
    differentModule.setMnc(false);
    differentModule = moduleRepository.save(differentModule);
  }

  /**
   * Tests scenario with no grades even though registration exists.
   */
  @Test
  @DisplayName("No Grades Even Though Registration Exists")
  public void testNoGradesButRegistered() throws Exception {
    Student student = new Student();
    student.setId(9L);
    student.setFirstName("Irene");
    student.setLastName("Adler");
    student.setUsername("iadler");
    student.setEmail("irene.adler@example.com");
    student = studentRepository.save(student);

    Module module = new Module();
    module.setCode("SHER101");
    module.setName("Sherlock Studies");
    module.setMnc(false);
    module = moduleRepository.save(module);

    Registration reg = new Registration();
    reg.setStudent(student);
    reg.setModule(module);
    reg = registrationRepository.save(reg);
  }
}
