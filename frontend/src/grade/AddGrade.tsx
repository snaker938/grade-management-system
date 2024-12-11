import React from 'react';
import axios from 'axios';
import {
  Paper,
  Button,
  Typography,
  Select,
  MenuItem,
  TextField,
  Alert,
} from '@mui/material';
import {
  AddGradeBody,
  EntityModelStudent,
  EntityModelModule,
} from '../api/index';
import { API_ENDPOINT } from '../config';

function AddGrade(props: { update: () => void }) {
  // State to hold the grade data
  const [grade, setGrade] = React.useState<AddGradeBody>({});
  // State to hold the list of students
  const [students, setStudents] = React.useState<EntityModelStudent[]>([]);
  // State to hold the list of modules
  const [modules, setModules] = React.useState<EntityModelModule[]>();
  // State to hold any error messages
  const [error, setError] = React.useState<string>();

  // Fetch students and modules when the component mounts
  React.useEffect(() => {
    axios
      .get(`${API_ENDPOINT}/students`)
      .then((response) => {
        setStudents(response.data._embedded.students);
      })
      .catch((response) => setError(response.message));

    axios
      .get(`${API_ENDPOINT}/modules`)
      .then((response) => setModules(response.data._embedded.modules))
      .catch((response) => setError(response.message));
  }, []);

  // Function to handle the request to add a grade
  function request() {
    axios
      .post(`${API_ENDPOINT}/grades/addGrade`, grade)
      .then(() => {
        props.update();
      })
      .catch((response) => {
        setError(response.message);
      });
  }

  return (
    <Paper sx={{ padding: '30px' }}>
      <Typography variant="h5">Add Grade</Typography>
      <br />
      <br />
      {/* Select input for choosing a student */}
      <Select
        sx={{ minWidth: '300px' }}
        value={grade.student_id ?? ''}
        onChange={(e) => setGrade({ ...grade, student_id: e.target.value })}
        label="Student"
      >
        {students &&
          students.map((s) => {
            return (
              <MenuItem
                key={s.id}
                value={s.id}
              >{`${s.firstName} ${s.lastName} (${s.id})`}</MenuItem>
            );
          })}
      </Select>
      {/* Select input for choosing a module */}
      <Select
        sx={{ minWidth: '300px' }}
        value={grade.module_code ?? ''}
        onChange={(e) => setGrade({ ...grade, module_code: e.target.value })}
        label="Module"
      >
        {modules &&
          modules.map((m) => {
            return (
              <MenuItem
                key={m.code}
                value={m.code}
              >{`${m.code} ${m.name}`}</MenuItem>
            );
          })}
      </Select>
      {/* TextField input for entering the score */}
      <TextField
        label="Score"
        value={grade.score ?? 0}
        onChange={(e) => setGrade({ ...grade, score: e.target.value })}
      />
      <br />
      <br />
      {/* Button to submit the grade */}
      <Button onClick={request}>Add</Button>
      <br />
      <br />
      {/* Display error message if any */}
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddGrade;