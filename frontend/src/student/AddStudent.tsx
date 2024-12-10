import React from "react";
import axios from "axios";
import { Paper, TextField, Button, Typography, Alert } from "@mui/material";
import { EntityModelStudent } from "../api/entityModelStudent.ts";
import { API_ENDPOINT } from "../config";

function AddStudent(props: { update: Function }) {
  const [student, setStudent] = React.useState<EntityModelStudent>({});
  const [error, setError] = React.useState<string>();

  function request() {
    axios
      .post(`${API_ENDPOINT}/students`, student)
      .then(() => {
        props.update();
      })
      .catch((response) => {
        setError(response.message);
      });
  }

  return (
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Add/Update Student</Typography>
      <br />
      <TextField
        label="Student ID"
        onChange={(e) => {
          setStudent({ ...student, id: Number(e.target.value) });
        }}
      />
      <TextField
        label="Username"
        onChange={(e) => {
          setStudent({ ...student, username: e.target.value });
        }}
      />
      <TextField
        label="email"
        onChange={(e) => {
          setStudent({ ...student, email: e.target.value });
        }}
      />
      <br />
      <br />
      <TextField
        label="First Name"
        onChange={(e) => {
          setStudent({ ...student, firstName: e.target.value });
        }}
      />
      <TextField
        label="Last Name"
        onChange={(e) => {
          setStudent({ ...student, lastName: e.target.value });
        }}
      />
      <br />
      <br />
      <Button onClick={request}>Add/Update</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddStudent;
