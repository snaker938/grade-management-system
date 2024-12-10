import React from "react";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid } from "@mui/material";
import App from "../App.tsx";
import { EntityModelStudent } from "../api/index";
import { API_ENDPOINT } from "../config";
import AddStudent from "./AddStudent";

function Students() {
  const [students, setStudents] = React.useState<EntityModelStudent[]>([]);
  const [error, setError] = React.useState<string>();

  React.useEffect(() => {
    updateStudents();
  }, []);

  function updateStudents() {
    axios
      .get(`${API_ENDPOINT}/students`)
      .then((response) => {
        setStudents(response.data._embedded.students);
      })
      .catch((response) => {
        setError(response.message);
      });
  }

  return (
    <App>
      <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
          Home
        </Link>
        <Typography sx={{ color: "text.primary" }}>Students</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && students.length < 1 && (
        <Alert color="warning">No students</Alert>
      )}
      {students.length > 0 && (
        <>
          <Grid container style={{ padding: "10px 0" }}>
            <Grid item xs={2}>
              Student ID
            </Grid>
            <Grid item xs={2}>
              First Name
            </Grid>
            <Grid item xs={2}>
              Last Name
            </Grid>
            <Grid item xs={2}>
              Username
            </Grid>
            <Grid item xs={4}>
              email
            </Grid>
          </Grid>
          {students.map((s) => {
            return (
              <Grid container key={s.id} style={{ padding: "10px 0" }}>
                <Grid item xs={2}>
                  {s.id}
                </Grid>
                <Grid item xs={2}>
                  {s.firstName}
                </Grid>
                <Grid item xs={2}>
                  {s.lastName}
                </Grid>
                <Grid item xs={2}>
                  {s.username}
                </Grid>
                <Grid item xs={4}>
                  {s.email}
                </Grid>
              </Grid>
            );
          })}
        </>
      )}
      <br />
      <br />
      <AddStudent update={updateStudents} />
    </App>
  );
}

export default Students;
