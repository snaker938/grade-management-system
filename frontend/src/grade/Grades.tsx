import React from "react";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid } from "@mui/material";
import App from "../App.tsx";
import {
  EntityModelGrade,
  EntityModelStudent,
  EntityModelModule,
} from "../api/index";
import { API_ENDPOINT } from "../config";
import AddGrade from "./AddGrade";

function GradeRow(props: { grade: EntityModelGrade }) {
  const { grade } = props;
  const [student, setStudent] = React.useState<EntityModelStudent>();
  const [module, setModule] = React.useState<EntityModelModule>();

  React.useEffect(() => {
    axios
      .get(grade._links!.module!.href!)
      .then((response) => setModule(response.data));

    axios
      .get(grade._links!.student!.href!)
      .then((response) => setStudent(response.data));
  }, [grade]);

  return (
    <Grid key={grade.id} container style={{ padding: "10px 0" }}>
      <Grid item xs={4}>
        {student && `${student.firstName} ${student.lastName} (${student.id})`}
      </Grid>
      <Grid item xs={4}>
        {module && `${module.code} ${module.name}`}
      </Grid>
      <Grid item xs={4}>
        {grade.score}
      </Grid>
    </Grid>
  );
}

function Grades() {
  const [grades, setGrades] = React.useState<EntityModelGrade[]>([]);
  const [error, setError] = React.useState<string>();

  React.useEffect(() => {
    updateGrades();
  }, []);

  function updateGrades() {
    axios
      .get(`${API_ENDPOINT}/grades`)
      .then((response) => {
        setGrades(response.data._embedded.grades);
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
        <Typography sx={{ color: "text.primary" }}>Grades</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && grades.length < 1 && <Alert color="warning">No grades</Alert>}
      {grades.length > 0 && (
        <>
          <Grid container style={{ padding: "10px 0" }}>
            <Grid item xs={4}>
              Student
            </Grid>
            <Grid item xs={4}>
              Module
            </Grid>
            <Grid item xs={4}>
              Score
            </Grid>
          </Grid>
          {grades.map((g) => {
            return <GradeRow grade={g} />;
          })}
        </>
      )}
      <br />
      <br />
      <AddGrade update={updateGrades} />
    </App>
  );
}

export default Grades;
