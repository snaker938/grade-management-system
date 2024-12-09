package uk.ac.ucl.comp0010.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Tests for the GradeController.
 * Verifies that adding a grade for a given student and module returns a valid
 * Grade object.
 */
class GradeControllerTest {

  /**
   * Tests that adding a grade via the controller returns a non-null Grade entity.
   */
  @Test
  @DisplayName("Add Grade Successfully")
  void testAddGrade() {
    // Mock the repositories
    StudentRepository mockStudentRepo = Mockito.mock(StudentRepository.class);
    ModuleRepository mockModuleRepo = Mockito.mock(ModuleRepository.class);
    GradeRepository mockGradeRepo = Mockito.mock(GradeRepository.class);

    // Setup returned entities
    Student s = new Student();
    s.setId(1L);
    Module m = new Module();
    m.setCode("MODX");

    // Mock repository methods
    when(mockStudentRepo.findById(1L)).thenReturn(java.util.Optional.of(s));
    when(mockModuleRepo.findById("MODX")).thenReturn(java.util.Optional.of(m));
    when(mockGradeRepo.save(any(Grade.class))).thenAnswer(i -> i.getArgument(0));

    // Create controller with mocked repos
    GradeController ctrl = new GradeController(mockStudentRepo, mockModuleRepo, mockGradeRepo);

    // Input parameters for adding a grade
    Map<String, String> params = new HashMap<>();
    params.put("student_id", "1");
    params.put("module_code", "MODX");
    params.put("score", "95");
    params.put("academic_year", "2024/2025");

    // Call the controller method
    ResponseEntity<Grade> response = ctrl.addGrade(params);

    // Verify the returned Grade is not null
    assertNotNull(response.getBody());
  }
}
