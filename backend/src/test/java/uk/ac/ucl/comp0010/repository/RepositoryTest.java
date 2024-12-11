package uk.ac.ucl.comp0010.repository;

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

/**
 * Tests basic CRUD operations and repository context loading.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
@DisplayName("Repository Tests")
public final class RepositoryTest {

  @Autowired
  StudentRepository studentRepository;

  @Autowired
  ModuleRepository moduleRepository;

  @Autowired
  GradeRepository gradeRepository;

  @Autowired
  RegistrationRepository registrationRepository;

  /**
   * Ensures a clean state before each test.
   */
  @BeforeEach
  public void setUp() {
  }

  /**
   * Verifies that the repositories are correctly loaded.
   */
  @Test
  @DisplayName("Context Loads and Repositories Are Not Null")
  public void contextLoads() {
    assertNotNull(studentRepository);
    assertNotNull(moduleRepository);
    assertNotNull(gradeRepository);
    assertNotNull(registrationRepository);
  }

  /**
   * Tests basic CRUD operations on the Student repository.
   */
  @Test
  @DisplayName("Student Repository CRUD Operations")
  public void testStudentCrud() {
    Student student = new Student();
    student.setId(10L);
    student.setFirstName("Test");
    student.setLastName("User");
    student.setUsername("tuser");
    student.setEmail("t.user@example.com");

    student = studentRepository.save(student);
    assertNotNull(studentRepository.findById(10L).orElse(null), "Student should be saved and found by ID.");

    studentRepository.deleteById(10L);
    assertFalse(studentRepository.findById(10L).isPresent(), "Student should be deleted.");
  }

  /**
   * Tests basic CRUD operations on the Module repository.
   */
  @Test
  @DisplayName("Module Repository CRUD Operations")
  public void testModuleCrud() {
    Module module = new Module();
    module.setCode("TESTMOD");
    module.setName("Test Module");
    module.setMnc(false);

    module = moduleRepository.save(module);
    assertTrue(moduleRepository.findById("TESTMOD").isPresent(), "Module should be saved and found by code.");

    moduleRepository.deleteById("TESTMOD");
    assertFalse(moduleRepository.findById("TESTMOD").isPresent(), "Module should be deleted.");
  }

  /**
   * Tests basic CRUD operations on the Registration repository.
   */
  @Test
  @DisplayName("Registration Repository CRUD Operations")
  public void testRegistrationCrud() {
    Student student = new Student();
    student.setId(20L);
    student.setFirstName("Reg");
    student.setLastName("Test");
    student.setUsername("rtest");
    student.setEmail("r.test@example.com");
    studentRepository.save(student);

    Module module = new Module();
    module.setCode("REGMOD");
    module.setName("Registration Module");
    module.setMnc(true);
    moduleRepository.save(module);

    Registration registration = new Registration();
    registration.setStudent(student);
    registration.setModule(module);
    registration = registrationRepository.save(registration);

    assertNotNull(registration.getId(), "Registration should be saved and have an ID.");
    assertTrue(registrationRepository.findById(registration.getId()).isPresent(),
        "Registration should be found by ID.");

    registrationRepository.deleteById(registration.getId());
    assertFalse(registrationRepository.findById(registration.getId()).isPresent(), "Registration should be deleted.");
  }

  /**
   * Tests basic CRUD operations on the Grade repository.
   */
  @Test
  @DisplayName("Grade Repository CRUD Operations")
  public void testGradeCrud() {
    Student student = new Student();
    student.setId(30L);
    student.setFirstName("Grade");
    student.setLastName("Tester");
    student.setUsername("gtester");
    student.setEmail("g.tester@example.com");
    studentRepository.save(student);

    Module module = new Module();
    module.setCode("GRDMOD");
    module.setName("Grade Module");
    module.setMnc(false);
    moduleRepository.save(module);

    Grade grade = new Grade();
    grade.setScore(85);
    grade.setAcademicYear("2024/2025");
    grade.setStudent(student);
    grade.setModule(module);
    grade = gradeRepository.save(grade);

    assertNotNull(grade.getId(), "Grade should be saved and have an ID.");
    assertTrue(gradeRepository.findById(grade.getId()).isPresent(), "Grade should be found by ID.");

    gradeRepository.deleteById(grade.getId());
    assertFalse(gradeRepository.findById(grade.getId()).isPresent(), "Grade should be deleted.");
  }
}
