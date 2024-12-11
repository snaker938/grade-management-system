package uk.ac.ucl.comp0010.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller for managing modules, including registering/removing students
 * and retrieving enrollment data.
 */
@RestController
@RequestMapping("/modules")
public final class ModuleController {

  /** Repository for Module entities. */
  private final ModuleRepository moduleRepository;
  /** Repository for Student entities. */
  private final StudentRepository studentRepository;
  /** Repository for Registration entities. */
  private final RegistrationRepository registrationRepository;
  /** Repository for Grade entities. */
  private final GradeRepository gradeRepository;

  /**
   * Constructs a ModuleController with the required repositories.
   *
   * @param modRepo  the repository for Module entities
   * @param studRepo the repository for Student entities
   * @param regRepo  the repository for Registration entities
   * @param gRepo    the repository for Grade entities
   */
  public ModuleController(final ModuleRepository modRepo,
      final StudentRepository studRepo,
      final RegistrationRepository regRepo,
      final GradeRepository gRepo) {
    this.moduleRepository = modRepo;
    this.studentRepository = studRepo;
    this.registrationRepository = regRepo;
    this.gradeRepository = gRepo;

    // Use the gradeRepository to stop the IDE
    // from complaining about unused fields
    this.gradeRepository.findAll();
  }

  /**
   * Registers a student to a specified module if the module has available seats
   * and the student is not already registered.
   *
   * Expects "studentId" in the request body.
   *
   * @param code the module code
   * @param body a map containing "studentId"
   * @return OK if registration is successful,
   *         BAD_REQUEST if the module is full or the student is already
   *         registered,
   *         NOT_FOUND if the module or student does not exist
   */
  @PostMapping("/{code}/registerStudent")
  public ResponseEntity<?> registerStudent(
      @PathVariable final String code,
      @RequestBody final Map<String, String> body) {
    if (!body.containsKey("studentId")) {
      return ResponseEntity.badRequest().body("Missing 'studentId' parameter");
    }

    Long studentId = Long.valueOf(body.get("studentId"));
    Optional<Module> modOpt = moduleRepository.findById(code);
    Optional<Student> studOpt = studentRepository.findById(studentId);

    if (!modOpt.isPresent() || !studOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Module m = modOpt.get();
    Student s = studOpt.get();

    int enrolledCount = m.getRegistrations().size();
    if (enrolledCount >= m.getMaxSeats()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Module capacity reached");
    }

    boolean alreadyRegistered = m.getRegistrations().stream()
        .anyMatch(r -> r.getStudent().getId().equals(s.getId()));
    if (alreadyRegistered) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Student already registered in this module");
    }

    Registration r = new Registration();
    r.setStudent(s);
    r.setModule(m);
    registrationRepository.save(r);

    return ResponseEntity.ok().build();
  }

  /**
   * Removes a student from a specified module.
   *
   * @param code the module code
   * @param id   the ID of the student to remove
   * @return OK if removal is successful,
   *         BAD_REQUEST if the student is not registered in the module,
   *         NOT_FOUND if the module or student does not exist
   */
  @DeleteMapping("/{code}/students/{id}")
  public ResponseEntity<?> removeStudent(
      @PathVariable final String code,
      @PathVariable final Long id) {
    Optional<Module> modOpt = moduleRepository.findById(code);
    Optional<Student> studOpt = studentRepository.findById(id);

    if (!modOpt.isPresent() || !studOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Module m = modOpt.get();
    Student s = studOpt.get();

    Optional<Registration> regOpt = m.getRegistrations().stream()
        .filter(reg -> reg.getStudent().getId().equals(s.getId()))
        .findFirst();

    if (!regOpt.isPresent()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Student not registered in this module");
    }

    registrationRepository.delete(regOpt.get());
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves a list of enrolled students for a specified module
   * along with their grades (if available).
   *
   * Returns a JSON object containing "enrolledStudents" as a list
   * of student details, including:
   * - id, firstName, lastName, email
   * - grade and gradeId if available, otherwise null
   *
   * @param code the module code
   * @return OK with a JSON object containing the enrolled students and their
   *         grades, NOT_FOUND if the module does not exist
   */
  @GetMapping("/{code}/registrations")
  public ResponseEntity<?> getModuleRegistrations(
      @PathVariable final String code) {
    Optional<Module> modOpt = moduleRepository.findById(code);
    if (!modOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Module m = modOpt.get();
    List<Map<String, Object>> enrolledStudents = m.getRegistrations().stream()
        .map(reg -> {
          Student st = reg.getStudent();
          Optional<Grade> gOpt = m.getGrades().stream()
              .filter(gr -> gr.getStudent().getId().equals(st.getId()))
              .findFirst();

          Map<String, Object> stuMap = new HashMap<>();
          stuMap.put("id", st.getId());
          stuMap.put("firstName", st.getFirstName());
          stuMap.put("lastName", st.getLastName());
          stuMap.put("email", st.getEmail());
          stuMap.put("grade", gOpt.map(Grade::getScore).orElse(null));
          stuMap.put("gradeId", gOpt.map(Grade::getId).orElse(null));
          return stuMap;
        }).collect(Collectors.toList());

    Map<String, Object> result = new HashMap<>();
    result.put("enrolledStudents", enrolledStudents);
    return ResponseEntity.ok(result);
  }

  /**
   * Updates module information such as name, mandatory non-condonable (mnc)
   * field, and maxSeats.
   * Expects optional "name", "mnc", and "maxSeats" in the request body.
   *
   * @param code the module code
   * @param body a map containing optional "name", "mnc", and "maxSeats" values
   * @return OK with the updated module entity, or NOT_FOUND if the module does
   *         not exist
   */
  @PutMapping("/{code}")
  public ResponseEntity<Module> updateModule(
      @PathVariable final String code,
      @RequestBody final Map<String, String> body) {
    Optional<Module> modOpt = moduleRepository.findById(code);
    if (!modOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Module m = modOpt.get();

    if (body.containsKey("name")) {
      m.setName(body.get("name"));
    }
    if (body.containsKey("mnc")) {
      m.setMnc(Boolean.parseBoolean(body.get("mnc")));
    }
    if (body.containsKey("maxSeats")) {
      int ms = Integer.parseInt(body.get("maxSeats"));
      if (ms > 0) {
        m.setMaxSeats(ms);
      }
    }

    moduleRepository.save(m);
    return ResponseEntity.ok(m);
  }
}
