import React from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Grid } from "@mui/material";
import axios from "axios";
import { API_ENDPOINT } from "../config";
import { EntityModelStudent } from "../api/index";

interface InspectStudentProps {
  open: boolean;
  onClose: () => void;
  student: EntityModelStudent;
  update: () => void;
}

/**
 * InspectStudent component allows editing an existing student's details.
 * After saving, calls `update()` to refresh the main list.
 */
function InspectStudent({ open, onClose, student, update }: InspectStudentProps) {
  const [id, setId] = React.useState<string>(student.id?.toString() ?? "");
  const [username, setUsername] = React.useState(student.username ?? "");
  const [email, setEmail] = React.useState(student.email ?? "");
  const [firstName, setFirstName] = React.useState(student.firstName ?? "");
  const [lastName, setLastName] = React.useState(student.lastName ?? "");

  React.useEffect(() => {
    // Reset fields when the student or open changes
    if (open) {
      setId(student.id?.toString() ?? "");
      setUsername(student.username ?? "");
      setEmail(student.email ?? "");
      setFirstName(student.firstName ?? "");
      setLastName(student.lastName ?? "");
    }
  }, [open, student]);

  function handleSave() {
    const updatedStudent: Partial<EntityModelStudent> = {
      id: id ? parseInt(id, 10) : undefined,
      username,
      email,
      firstName,
      lastName,
    };

    if (!updatedStudent.id) {
      return; // can't update without ID
    }

    axios
      .put(`${API_ENDPOINT}/students/${updatedStudent.id}`, updatedStudent)
      .then(() => {
        update();
        onClose();
      })
      .catch((err) => {
        console.error(err);
      });
  }

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Edit Student</DialogTitle>
      <DialogContent dividers>
        <Grid container spacing={2} sx={{ marginTop: "10px" }}>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Student ID"
              variant="outlined"
              size="small"
              value={id}
              disabled // ID usually not changed
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Username"
              variant="outlined"
              size="small"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Email"
              variant="outlined"
              size="small"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="First Name"
              variant="outlined"
              size="small"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Last Name"
              variant="outlined"
              size="small"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </Grid>
        </Grid>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="inherit">
          Cancel
        </Button>
        <Button onClick={handleSave} variant="contained" color="primary">
          Save Changes
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default InspectStudent;
