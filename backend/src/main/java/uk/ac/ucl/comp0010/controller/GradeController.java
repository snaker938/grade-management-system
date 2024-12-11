package uk.ac.ucl.comp0010.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller for handling Grade-related operations.
 * Provides endpoints for adding and updating a Grade.
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
   * Expects "student_id", "module_code", "score", and "academic_year" in the
   * request body.
   * Ensures the student is registered in the module before adding the grade.
   *
   * @param params a map of parameter names to values:
   *               "student_id", "module_code", "score", "academic_year"
   * @return a ResponseEntity containing the saved Grade if successful,
   *         BAD_REQUEST if parameters are missing or the student is not enrolled,
   *         NOT_FOUND if the student or module does not exist.
   */
  @PostMapping("/grades/addGrade")
  public ResponseEntity<Grade> addGrade(@RequestBody Map<String, String> params) {
    String studentIdStr = params.get("student_id");
    String moduleCode = params.get("module_code");
    String scoreStr = params.get("score");
    String academicYear = params.get("academic_year");

    if (studentIdStr == null || moduleCode == null || scoreStr == null || academicYear == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Long studentId = Long.valueOf(studentIdStr);
    Optional<Student> studentOpt = studentRepository.findById(studentId);
    if (!studentOpt.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Optional<Module> moduleOpt = moduleRepository.findById(moduleCode);
    if (!moduleOpt.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Student student = studentOpt.get();
    Module module = moduleOpt.get();

    boolean registered = student.getRegistrations().stream()
        .anyMatch(r -> r.getModule().getCode().equals(module.getCode()));

    if (!registered) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Grade g = new Grade();
    g.setStudent(student);
    g.setModule(module);
    g.setScore(Integer.parseInt(scoreStr));
    g.setAcademicYear(academicYear);

    Grade savedGrade = gradeRepository.save(g);

    return new ResponseEntity<>(savedGrade, HttpStatus.OK);
  }

  /**
   * Updates an existing Grade.
   * Expects optional "score" and "academic_year" in the request body.
   * If provided, these fields are updated accordingly.
   *
   * @param id     the ID of the grade to update
   * @param params a map of parameters that may include "score" and/or
   *               "academic_year"
   * @return a ResponseEntity containing the updated Grade, or NOT_FOUND if no
   *         such Grade exists.
   */
  @PutMapping("/grades/{id}")
  public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Map<String, String> params) {
    Optional<Grade> gradeOpt = gradeRepository.findById(id);
    if (!gradeOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Grade grade = gradeOpt.get();

    if (params.containsKey("score")) {
      grade.setScore(Integer.parseInt(params.get("score")));
    }
    if (params.containsKey("academic_year")) {
      grade.setAcademicYear(params.get("academic_year"));
    }

    Grade saved = gradeRepository.save(grade);
    return ResponseEntity.ok(saved);
  }
}
