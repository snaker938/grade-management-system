import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Alert,
} from '@mui/material';
import axios from 'axios';
import { API_ENDPOINT } from '../config';

// Define the props for the AddOrEditGrade component
interface AddOrEditGradeProps {
  open: boolean;
  onClose: () => void;
  student: { id: number; firstName: string; lastName: string }; // {id, firstName, lastName, ...}
  moduleCode: string;
  update: () => void;
  grade?: { id: number; score: number; academicYear: string }; // {id, score, academicYear} if editing, otherwise undefined
}

function AddOrEditGrade({
  open,
  onClose,
  student,
  moduleCode,
  update,
  grade,
}: AddOrEditGradeProps) {
  // State to manage error messages
  const [error, setError] = React.useState<string>();

  // Define the structure of the grade data
  interface GradeData {
    score: string | number;
    academic_year: string;
  }

  // State to manage the grade data
  const [gradeData, setGradeData] = React.useState<GradeData>({
    score: grade?.score ?? '',
    academic_year: grade?.academicYear ?? '',
  });

  // Determine if we are editing an existing grade
  const isEditing = !!grade?.id;

  // Function to save the grade
  function saveGrade() {
    // Validate required fields
    if (!student.id || !moduleCode) {
      setError('Missing required student or module code.');
      return;
    }

    if (!gradeData.score || !gradeData.academic_year) {
      setError('Please provide a score and academic year.');
      return;
    }

    // Prepare the payload for the API request
    const payload = {
      student_id: student.id,
      module_code: moduleCode,
      score: parseInt(gradeData.score.toString(), 10),
      academic_year: gradeData.academic_year,
    };

    // If editing, send a PUT request to update the grade
    if (isEditing && grade.id) {
      axios
        .put(`${API_ENDPOINT}/grades/${grade.id}`, {
          score: payload.score,
          academic_year: payload.academic_year,
        })
        .then(() => {
          update();
          onClose();
        })
        .catch((res) => setError(res.message));
    } else {
      // If adding a new grade, send a POST request
      axios
        .post(`${API_ENDPOINT}/grades/addGrade`, payload)
        .then(() => {
          update();
          onClose();
        })
        .catch((res) => setError(res.message));
    }
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{isEditing ? 'Edit Grade' : 'Add Grade'}</DialogTitle>
      <DialogContent>
        {error && (
          <Alert severity="error" sx={{ marginBottom: '20px' }}>
            {error}
          </Alert>
        )}
        <TextField
          fullWidth
          variant="outlined"
          label="Score"
          type="number"
          sx={{ marginBottom: '20px' }}
          value={gradeData.score}
          onChange={(e) =>
            setGradeData({ ...gradeData, score: e.target.value })
          }
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Academic Year (e.g. 2024/2025)"
          sx={{ marginBottom: '20px' }}
          value={gradeData.academic_year}
          onChange={(e) =>
            setGradeData({ ...gradeData, academic_year: e.target.value })
          }
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={saveGrade} variant="contained">
          {isEditing ? 'Save' : 'Add'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default AddOrEditGrade;
