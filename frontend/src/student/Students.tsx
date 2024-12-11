import React from "react";
import axios from "axios";
import { 
 
  Typography, 
  Alert, 
  TextField, 
  IconButton, 
  Container, 
  Card, 
  Divider, 
  Grid 
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import SearchIcon from "@mui/icons-material/Search";
import EditIcon from "@mui/icons-material/Edit";

import App from "../App";
import Navbar from "../components/Navbar";
import AddStudent from "./AddStudent";
import InspectStudent from "./InspectStudent";
import { EntityModelStudent } from "../api/index";
import { API_ENDPOINT } from "../config";

/**
 * The Students page allows:
 * - Adding/Updating a student via a top form (AddStudent).
 * - Searching and listing students in a scrollable area.
 * - Inspecting (editing) a selected student.
 */
function Students() {
  const [students, setStudents] = React.useState<EntityModelStudent[]>([]);
  const [filteredStudents, setFilteredStudents] = React.useState<
    EntityModelStudent[]
  >([]);
  const [error, setError] = React.useState<string>();
  const [searchTerm, setSearchTerm] = React.useState("");

  const [showInspect, setShowInspect] = React.useState(false);
  const [selectedStudent, setSelectedStudent] = React.useState<EntityModelStudent>();

  React.useEffect(() => {
    updateStudents();
  }, []);

  React.useEffect(() => {
    const term = searchTerm.toLowerCase();
    const filtered = students.filter(
      (s) =>
        (s.id && s.id.toString().includes(term)) ||
        s.firstName?.toLowerCase().includes(term) ||
        s.lastName?.toLowerCase().includes(term) ||
        s.username?.toLowerCase().includes(term) ||
        s.email?.toLowerCase().includes(term)
    );
    setFilteredStudents(filtered);
  }, [searchTerm, students]);

  function updateStudents() {
    axios
      .get(`${API_ENDPOINT}/students`)
      .then((response) => {
        setStudents(response.data._embedded?.students ?? []);
      })
      .catch((err) => {
        setError(err.message);
      });
  }

  function handleInspect(s: EntityModelStudent) {
    setSelectedStudent(s);
    setShowInspect(true);
  }

  return (
    <>
      <Navbar currentPage="students" />
      <App>
        <Container maxWidth="md" sx={{ paddingTop: "120px" }}>

          {error && <Alert severity="error" sx={{ marginBottom: "20px" }}>{error}</Alert>}

          {/* Add/Update Student Form */}
          <Card elevation={2} sx={{ padding: "20px", marginBottom: "30px" }}>
            <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: "20px" }}>
              Add Student
            </Typography>
            <AddStudent update={updateStudents} />
          </Card>

          <Divider sx={{ marginBottom: "20px" }} />

          {/* Search and Refresh Row */}
          <Grid container spacing={2} alignItems="center" sx={{ marginBottom: "20px" }}>
            <Grid item xs={12} md={8}>
              <TextField
                variant="outlined"
                size="small"
                placeholder="Search students..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                fullWidth
                InputProps={{
                  endAdornment: <SearchIcon color="action" />
                }}
              />
            </Grid>
            <Grid item xs={12} md={4} sx={{ textAlign: "right" }}>
              <IconButton onClick={updateStudents} title="Refresh">
                <RefreshIcon />
              </IconButton>
            </Grid>
          </Grid>

          {/* Students List */}
          {filteredStudents.length < 1 && !error && (
            <Alert severity="warning">No students</Alert>
          )}

          {filteredStudents.length > 0 && (
            <div style={{ maxHeight: "300px", overflowY: "auto", border: "1px solid #ddd", borderRadius: "8px" }}>
              <Grid container sx={{ background: "#f0f0f0", padding: "10px 0", fontWeight: 600 }}>
                <Grid item xs={2}>ID</Grid>
                <Grid item xs={2}>First Name</Grid>
                <Grid item xs={2}>Last Name</Grid>
                <Grid item xs={2}>Username</Grid>
                <Grid item xs={3}>Email</Grid>
                <Grid item xs={1} style={{ textAlign: "right" }}>Edit</Grid>
              </Grid>
              {filteredStudents.map((s) => (
                <Grid container key={s.id} sx={{ padding: "10px 0", borderBottom: "1px solid #eee", fontSize: "0.95rem" }}>
                  <Grid item xs={2}>{s.id}</Grid>
                  <Grid item xs={2}>{s.firstName}</Grid>
                  <Grid item xs={2}>{s.lastName}</Grid>
                  <Grid item xs={2}>{s.username}</Grid>
                  <Grid item xs={3}>{s.email}</Grid>
                  <Grid item xs={1} style={{ textAlign: "right" }}>
                    <IconButton onClick={() => handleInspect(s)} title="Inspect/Edit">
                      <EditIcon />
                    </IconButton>
                  </Grid>
                </Grid>
              ))}
            </div>
          )}

          {showInspect && selectedStudent && (
            <InspectStudent
              open={showInspect}
              onClose={() => setShowInspect(false)}
              student={selectedStudent}
              update={updateStudents}
            />
          )}
        </Container>
      </App>
    </>
  );
}

export default Students;
