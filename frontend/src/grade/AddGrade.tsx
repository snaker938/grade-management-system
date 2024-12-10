import React from "react";
import axios from "axios";
import {
  Paper,
  Button,
  Typography,
  Select,
  MenuItem,
  TextField,
  Alert,
} from "@mui/material";
import {
  AddGradeBody,
  EntityModelStudent,
  EntityModelModule,
} from "../api/index";
import { API_ENDPOINT } from "../config";

function AddGrade(props: { update: Function }) {
  const [grade, setGrade] = React.useState<AddGradeBody>({});
  const [students, setStudents] = React.useState<EntityModelStudent[]>([]);
  const [modules, setModules] = React.useState<EntityModelModule[]>();
  const [error, setError] = React.useState<string>();

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
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Add Grade</Typography>
      <br />
      <br />
      <Select
        sx={{ minWidth: "300px" }}
        value={grade.student_id ?? ""}
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
      <Select
        sx={{ minWidth: "300px" }}
        value={grade.module_code ?? ""}
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
      <TextField
        label="Score"
        value={grade.score ?? 0}
        onChange={(e) => setGrade({ ...grade, score: e.target.value })}
      />
      <br />
      <br />
      <Button onClick={request}>Add</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddGrade;
