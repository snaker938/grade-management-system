package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

@DataJpaTest
public final class ModelIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;
  
  @Autowired
  private ModuleRepository moduleRepository;
  
  @Autowired
  private GradeRepository gradeRepository;
  
  @Autowired
  private RegistrationRepository registrationRepository;

  private Student student;
  private Module module;
  private Grade grade;
  private Registration registration;

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

    // Register the student to test registration logic
    student.registerModule(module);
    // Save registrations
    registrationRepository.saveAll(student.getRegistrations());

    grade = new Grade();
    grade.setScore(90);
    grade.setAcademicYear("2024/2025");
    grade.setStudent(student);
    grade.setModule(module);
    gradeRepository.save(grade);

    // For completeness, create a standalone registration
    registration = new Registration();
    registration.setStudent(student);
    registration.setModule(module);
    registrationRepository.save(registration);
  }

  @Test
  @DisplayName("Test Grade model getters/setters and toString")
  public void testGradeModel() {
    Grade g = new Grade();
    g.setId(10L);
    g.setScore(95);
    g.setAcademicYear("2025/2026");
    g.setStudent(student);
    g.setModule(module);

    assertEquals(10L, g.getId());
    assertEquals(95, g.getScore());
    assertEquals("2025/2026", g.getAcademicYear());
    assertEquals(student, g.getStudent());
    assertEquals(module, g.getModule());

    String toString = g.toString();
    assertNotNull(toString);
    // Just check some content in toString
    // e.g. "Grade{id=10"
    assert(toString.contains("id=10"));
    assert(toString.contains("score=95"));
  }

  @Test
  @DisplayName("Test Module model logic and mappings")
  public void testModuleModel() throws Exception {
    // Test getters/setters
    module.setCode("MODX");
    module.setName("Updated Name");
    module.setMnc(false);
    module.setMaxSeats(5);

    assertEquals("MODX", module.getCode());
    assertEquals("Updated Name", module.getName());
    assertEquals(false, module.isMnc());
    assertEquals(5, module.getMaxSeats());

    // Test computeAverageGrade
    List<Grade> gradeList = new ArrayList<>();
    Grade g1 = new Grade();
    g1.setScore(80);
    Grade g2 = new Grade();
    g2.setScore(100);
    gradeList.add(g1);
    gradeList.add(g2);

    float avg = module.computeAverageGrade(gradeList);
    assertEquals(90.0f, avg);

    // Test exception for no grades
    assertThrows(NoGradeAvailableException.class, () ->
        module.computeAverageGrade(Collections.emptyList()));

    // Check toString
    String toString = module.toString();
    assertNotNull(toString);
    assert(toString.contains("Module{code='MODX'"));
  }

  @Test
  @DisplayName("Test Registration model getters/setters and toString")
  public void testRegistrationModel() {
    Registration r = new Registration();
    r.setId(200L);
    r.setStudent(student);
    r.setModule(module);

    assertEquals(200L, r.getId());
    assertEquals(student, r.getStudent());
    assertEquals(module, r.getModule());

    String toString = r.toString();
    assertNotNull(toString);
    assert(toString.contains("Registration{id=200"));
  }

  @Test
  @DisplayName("Test Student model logic")
  public void testStudentModel() throws Exception {
    // Remove the line that sets a new ID, do not change the student's ID after saving
    // student.setId(2L); // Removed

    student.setFirstName("Jane");
    student.setLastName("Smith");
    student.setUsername("jsmith");
    student.setEmail("jsmith@example.com");

    // If the student was initially given id=1L in setUp, verify that instead of 2L
    assertEquals(1L, student.getId());
    assertEquals("Jane", student.getFirstName());
    assertEquals("Smith", student.getLastName());
    assertEquals("jsmith", student.getUsername());
    assertEquals("jsmith@example.com", student.getEmail());

    // Test registerModule
    Module newModule = new Module();
    newModule.setCode("MOD002");
    newModule.setName("Data Structures");
    newModule.setMaxSeats(3);
    moduleRepository.save(newModule);

    student.registerModule(newModule);
    // Save registrations again
    registrationRepository.saveAll(student.getRegistrations());

    assertEquals(2, student.getRegistrations().size());

    // Test addGrade when registered
    Grade newGrade = new Grade();
    newGrade.setScore(70);
    newGrade.setAcademicYear("2025/2026");
    newGrade.setModule(newModule);
    student.addGrade(newGrade); // should not throw
    gradeRepository.save(newGrade); // explicitly save the newly added grade

    // Test addGrade when not registered
    Module modX = new Module();
    modX.setCode("MODX");
    modX.setName("Physics");
    modX.setMaxSeats(2);
    moduleRepository.save(modX);

    Grade notRegisteredGrade = new Grade();
    notRegisteredGrade.setScore(50);
    notRegisteredGrade.setAcademicYear("2025/2027");
    notRegisteredGrade.setModule(modX);

    assertThrows(NoRegistrationException.class, () ->
        student.addGrade(notRegisteredGrade));

    // Test getGrade for a registered module with a grade
    Grade retrievedGrade = student.getGrade(newModule);
    assertEquals(70, retrievedGrade.getScore());

    // Test getGrade for a registered module with no grade
    Module modEmpty = new Module();
    modEmpty.setCode("MOD_EMPTY");
    modEmpty.setName("Empty");
    modEmpty.setMaxSeats(10);
    moduleRepository.save(modEmpty);

    student.registerModule(modEmpty);
    registrationRepository.saveAll(student.getRegistrations());
    assertThrows(NoGradeAvailableException.class, () ->
        student.getGrade(modEmpty));

    // Test getGrade for an unregistered module
    Module modUnreg = new Module();
    modUnreg.setCode("MOD_UNREG");
    modUnreg.setName("Unregistered");
    modUnreg.setMaxSeats(10);
    moduleRepository.save(modUnreg);

    assertThrows(NoRegistrationException.class, () ->
        student.getGrade(modUnreg));

    // Test computeAverage when grades are present
    float avg = student.computeAverage();
  
    assertEquals(70.0f, avg);

    // Test computeAverage with no grades
    Student emptyStudent = new Student();
    emptyStudent.setId(99L);
    studentRepository.save(emptyStudent);

    assertThrows(NoGradeAvailableException.class, () ->
        emptyStudent.computeAverage());

    // Check toString
    String toString = student.toString();
    assertNotNull(toString);
    assert(toString.contains("Student{id=1"));
  }

}
