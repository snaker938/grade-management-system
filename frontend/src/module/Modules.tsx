import React from "react";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid } from "@mui/material";
import App from "../App.tsx";
import { EntityModelModule } from "../api/entityModelModule.ts";
import { API_ENDPOINT } from "../config";
import AddModule from "./AddModule";

function Modules() {
  const [modules, setModules] = React.useState<EntityModelModule[]>([]);
  const [error, setError] = React.useState<string>();

  React.useEffect(() => {
    updateModules();
  }, []);

  function updateModules() {
    axios
      .get(`${API_ENDPOINT}/modules`)
      .then((response) => {
        setModules(response.data._embedded.modules);
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
        <Typography sx={{ color: "text.primary" }}>Modules</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && modules.length < 1 && (
        <Alert color="warning">No modules</Alert>
      )}
      {modules.length > 0 && (
        <>
          <Grid container style={{ padding: "10px 0" }}>
            <Grid item xs={2}>
              Module Code
            </Grid>
            <Grid item xs={8}>
              Module Name
            </Grid>
            <Grid item xs={2}>
              Is MNC
            </Grid>
          </Grid>
          {modules.map((m) => {
            return (
              <Grid container key={m.code} style={{ padding: "10px 0" }}>
                <Grid item xs={2}>
                  {m.code}
                </Grid>
                <Grid item xs={8}>
                  {m.name}
                </Grid>
                <Grid item xs={2}>
                  {m.mnc ? "Yes" : "No"}
                </Grid>
              </Grid>
            );
          })}
        </>
      )}
      <br />
      <br />
      <AddModule update={updateModules} />
    </App>
  );
}

export default Modules;
