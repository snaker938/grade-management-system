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

// Define the props for the EditStudent component
interface EditStudentProps {
  open: boolean;
  onClose: () => void;
  student: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    username: string;
  };
  update: () => void;
}

// EditStudent component definition
function EditStudent({ open, onClose, student, update }: EditStudentProps) {
  // State to manage the edited student details
  const [editStudent, setEditStudent] = React.useState<typeof student>(student);
  // State to manage error messages
  const [error, setError] = React.useState<string>();

  // Update the editStudent state when the student prop changes
  React.useEffect(() => {
    setEditStudent(student);
  }, [student]);

  // Function to save changes to the student details
  function saveChanges() {
    if (!editStudent.id) return;
    axios
      .put(`${API_ENDPOINT}/students/${editStudent.id}`, editStudent)
      .then(() => {
        update(); // Call the update function passed as a prop
        onClose(); // Close the dialog
      })
      .catch((res) => setError(res.message)); // Set error message if the request fails
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Edit Student</DialogTitle>
      <DialogContent>
        {error && (
          <Alert severity="error" sx={{ marginBottom: '20px' }}>
            {error}
          </Alert>
        )}
        <TextField
          fullWidth
          variant="outlined"
          label="First Name"
          sx={{ marginBottom: '20px' }}
          value={editStudent.firstName ?? ''}
          onChange={(e) =>
            setEditStudent({ ...editStudent, firstName: e.target.value })
          }
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Last Name"
          sx={{ marginBottom: '20px' }}
          value={editStudent.lastName ?? ''}
          onChange={(e) =>
            setEditStudent({ ...editStudent, lastName: e.target.value })
          }
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Email"
          sx={{ marginBottom: '20px' }}
          value={editStudent.email ?? ''}
          onChange={(e) =>
            setEditStudent({ ...editStudent, email: e.target.value })
          }
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Username"
          sx={{ marginBottom: '20px' }}
          value={editStudent.username ?? ''}
          onChange={(e) =>
            setEditStudent({ ...editStudent, username: e.target.value })
          }
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={saveChanges} variant="contained">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default EditStudent;
