package uk.ac.ucl.comp0010.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
 * Provides endpoints for adding and updating a Grade.
 */
@RestController
public final class GradeController {

  /** Repository for Student entities. */
  private final StudentRepository studentRepository;
  /** Repository for Module entities. */
  private final ModuleRepository moduleRepository;
  /** Repository for Grade entities. */
  private final GradeRepository gradeRepository;

  /**
   * Constructs a GradeController with the required repositories.
   *
   * @param studentRepo the repository for Student entities
   * @param moduleRepo  the repository for Module entities
   * @param gradeRepo   the repository for Grade entities
   */
  public GradeController(final StudentRepository studentRepo,
      final ModuleRepository moduleRepo, final GradeRepository gradeRepo) {
    this.studentRepository = studentRepo;
    this.moduleRepository = moduleRepo;
    this.gradeRepository = gradeRepo;
  }

  /**
   * Adds a new Grade based on the provided parameters.
   * Expects "student_id", "module_code", "score", and
   * "academic_year" in the request body. Ensures the student is
   * registered in the module before adding the grade.
   *
   * @param params a map of parameter names to values:
   *               "student_id", "module_code", "score",
   *               "academic_year"
   * @return a ResponseEntity containing the saved Grade if successful,
   *         BAD_REQUEST if parameters are missing or the student is
   *         not enrolled, NOT_FOUND if the student or module does not exist.
   */
  @PostMapping("/grades/addGrade")
  public ResponseEntity<Grade> addGrade(
      @RequestBody final Map<String, String> params) {
    String studentIdStr = params.get("student_id");
    String moduleCode = params.get("module_code");
    String scoreStr = params.get("score");
    String academicYear = params.get("academic_year");

    if (studentIdStr == null || moduleCode == null
        || scoreStr == null || academicYear == null) {
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
        .anyMatch(r -> r.getModule().getCode()
            .equals(module.getCode()));

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
   * @return a ResponseEntity containing the updated Grade, or NOT_FOUND
   *         if no such Grade exists.
   */
  @PutMapping("/grades/{id}")
  public ResponseEntity<Grade> updateGrade(
      @PathVariable final Long id,
      @RequestBody final Map<String, String> params) {
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
