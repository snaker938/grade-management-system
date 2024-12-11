import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  IconButton,
  Button,
  Grid,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  Alert,
  Divider,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import GradeIcon from '@mui/icons-material/Grade';
import { EntityModelModule } from '../api/entityModelModule';
import { API_ENDPOINT } from '../config';
import axios from 'axios';
import EditStudent from '../student/EditStudent';
import AddOrEditGrade from '../grade/AddOrEditGrade';

interface InspectModuleProps {
  open: boolean;
  onClose: () => void;
  module: EntityModelModule;
  update: () => void;
}

function InspectModule({ open, onClose, module, update }: InspectModuleProps) {
  interface Student {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    grade?: string;
    username: string;
  }

  // State to store all students from API
  const [students, setStudents] = React.useState<Student[]>([]);
  // State to store students enrolled in this module
  const [enrolled, setEnrolled] = React.useState<Student[]>([]);
  // State to store selected student ID for adding to module
  const [selectedStudentId, setSelectedStudentId] = React.useState('');
  // State to store error messages
  const [error, setError] = React.useState<string>();
  // State to control the visibility of the EditStudent modal
  const [showEditStudent, setShowEditStudent] = React.useState(false);
  // State to store data of the student being edited
  const [editStudentData, setEditStudentData] = React.useState<Student | null>(
    null
  );
  // State to control the visibility of the AddOrEditGrade modal
  const [showGradeModal, setShowGradeModal] = React.useState(false);
  // State to store data of the student being graded
  const [gradeStudent, setGradeStudent] = React.useState<Student | null>(null);

  // Function to fetch data from API
  const fetchData = React.useCallback(async () => {
    try {
      const [allStudentsRes, moduleDetailsRes] = await Promise.all([
        axios.get(`${API_ENDPOINT}/students`),
        axios.get(`${API_ENDPOINT}/modules/${module.code}/registrations`), // assume endpoint returns enrolled students + grades
      ]);

      setStudents(allStudentsRes.data._embedded.students);

      // moduleDetailsRes should contain enrolled students and their grades
      setEnrolled(moduleDetailsRes.data.enrolledStudents); // {id, firstName, lastName, gradeIfAny}
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError('An unknown error occurred');
      }
    }
  }, [module]);

  // Fetch data when component mounts or module changes
  React.useEffect(() => {
    fetchData();
  }, [module, fetchData]);

  // Function to add a student to the module
  function addStudentToModule() {
    if (!selectedStudentId) return;
    axios
      .post(`${API_ENDPOINT}/modules/${module.code}/registerStudent`, {
        studentId: selectedStudentId,
      })
      .then(() => {
        fetchData();
        update();
      })
      .catch((err) => setError(err.message));
  }

  // Function to remove a student from the module
  function removeStudentFromModule(s: Student) {
    axios
      .delete(`${API_ENDPOINT}/modules/${module.code}/students/${s.id}`)
      .then(() => {
        fetchData();
        update();
      })
      .catch((err) => setError(err.message));
  }

  // Function to open the EditStudent modal with the selected student's data
  function editStudentInfo(s: Student) {
    setEditStudentData(s);
    setShowEditStudent(true);
  }

  // Function to open the AddOrEditGrade modal with the selected student's data
  function handleGrade(s: Student) {
    setGradeStudent(s);
    setShowGradeModal(true);
  }

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>Inspect Module: {module.code}</DialogTitle>
      <DialogContent>
        {error && (
          <Alert severity="error" sx={{ marginBottom: '20px' }}>
            {error}
          </Alert>
        )}

        <Typography variant="body1" sx={{ marginBottom: '20px' }}>
          Name: {module.name} | MNC: {module.mnc ? 'Yes' : 'No'}
          <br />
          Enrolled: {module.enrolledCount ?? 0}/{module.maxSeats ?? 0}
        </Typography>

        <Divider sx={{ marginY: '20px' }} />

        <Typography variant="h6" sx={{ marginBottom: '20px' }}>
          Enrolled Students
        </Typography>
        {enrolled.length < 1 && (
          <Alert severity="info">No students enrolled.</Alert>
        )}
        {enrolled.length > 0 && (
          <>
            <Grid container className="enrolled-header">
              <Grid item xs={3}>
                Name
              </Grid>
              <Grid item xs={3}>
                Email
              </Grid>
              <Grid item xs={2}>
                Grade
              </Grid>
              <Grid item xs={4} sx={{ textAlign: 'right' }}>
                Actions
              </Grid>
            </Grid>
            {enrolled.map((st: Student) => (
              <Grid container key={st.id} className="enrolled-row">
                <Grid item xs={3}>
                  {st.firstName} {st.lastName}
                </Grid>
                <Grid item xs={3}>
                  {st.email}
                </Grid>
                <Grid item xs={2}>
                  {st.grade ?? 'N/A'}
                </Grid>
                <Grid item xs={4} sx={{ textAlign: 'right' }}>
                  <IconButton
                    onClick={() => editStudentInfo(st)}
                    title="Edit Student"
                    size="small"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => removeStudentFromModule(st)}
                    title="Remove from module"
                    size="small"
                  >
                    <DeleteIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleGrade(st)}
                    title={st.grade ? 'Edit Grade' : 'Add Grade'}
                    size="small"
                  >
                    <GradeIcon />
                  </IconButton>
                </Grid>
              </Grid>
            ))}
          </>
        )}

        <Divider sx={{ marginY: '20px' }} />

        {(module.enrolledCount ?? 0) < (module.maxSeats ?? 0) && (
          <>
            <Typography variant="h6" sx={{ marginBottom: '10px' }}>
              Assign New Student
            </Typography>
            <FormControl fullWidth sx={{ marginBottom: '20px' }}>
              <InputLabel>Student</InputLabel>
              <Select
                label="Student"
                value={selectedStudentId}
                onChange={(e) => setSelectedStudentId(e.target.value as string)}
              >
                {students.map((st: Student) => (
                  <MenuItem key={st.id} value={st.id}>
                    {st.firstName} {st.lastName} ({st.id})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button variant="contained" onClick={addStudentToModule}>
              Add to Module
            </Button>
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Close</Button>
      </DialogActions>

      {showEditStudent && editStudentData && (
        <EditStudent
          open={showEditStudent}
          onClose={() => setShowEditStudent(false)}
          student={editStudentData}
          update={() => {
            fetchData();
            update();
          }}
        />
      )}

      {showGradeModal && gradeStudent && (
        <AddOrEditGrade
          open={showGradeModal}
          onClose={() => setShowGradeModal(false)}
          student={gradeStudent}
          moduleCode={module.code ?? ''}
          update={() => {
            fetchData();
            update();
          }}
        />
      )}
    </Dialog>
  );
}

export default InspectModule;
