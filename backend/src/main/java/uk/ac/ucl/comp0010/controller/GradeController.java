package uk.ac.ucl.comp0010.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller for handling Grade-related operations.
 * Provides an endpoint for adding a new Grade.
 */
@RestController
public class GradeController {

  private final StudentRepository studentRepository;
  private final ModuleRepository moduleRepository;
  private final GradeRepository gradeRepository;

  /**
   * Constructs a GradeController with the required repositories.
   * 
   * @param studentRepository the repository for Student entities
   * @param moduleRepository  the repository for Module entities
   * @param gradeRepository   the repository for Grade entities
   */
  public GradeController(StudentRepository studentRepository,
      ModuleRepository moduleRepository,
      GradeRepository gradeRepository) {
    this.studentRepository = studentRepository;
    this.moduleRepository = moduleRepository;
    this.gradeRepository = gradeRepository;
  }

  /**
   * Adds a new Grade based on the provided parameters.
   * Expects a student_id, module_code, score, and academic_year.
   * 
   * @param params a map of parameter names to values
   * @return a ResponseEntity containing the saved Grade
   */
  @PostMapping(value = "/grades/addGrade")
  public ResponseEntity<Grade> addGrade(@RequestBody Map<String, String> params) {
    // Parse parameters
    String studentIdStr = params.get("student_id");
    String moduleCode = params.get("module_code");
    String scoreStr = params.get("score");
    String academicYear = params.get("academic_year");

    // Find student
    Long studentId = Long.valueOf(studentIdStr);
    Optional<Student> studentOpt = studentRepository.findById(studentId);
    if (!studentOpt.isPresent()) {
      // If no student found, return 404 Not Found or some error response
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Find module
    Optional<Module> moduleOpt = moduleRepository.findById(moduleCode);
    if (!moduleOpt.isPresent()) {
      // If no module found, return 404 Not Found or some error response
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create and save grade
    Grade g = new Grade();
    g.setStudent(studentOpt.get());
    g.setModule(moduleOpt.get());
    g.setScore(Integer.parseInt(scoreStr));
    g.setAcademicYear(academicYear);

    Grade savedGrade = gradeRepository.save(g);

    // Return the saved Grade
    return new ResponseEntity<>(savedGrade, HttpStatus.OK);
  }
}
