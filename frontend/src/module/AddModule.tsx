import React from 'react';
import axios from 'axios';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Switch,
  FormControlLabel,
  Button,
  Alert,
} from '@mui/material';
import { EntityModelModule } from '../api/entityModelModule';
import { API_ENDPOINT } from '../config';

// Define the AddModule component
function AddModule(props: {
  open: boolean;
  onClose: () => void;
  update: () => void;
}) {
  // State to hold the module data
  const [module, setModule] = React.useState<EntityModelModule>({});
  // State to hold any error messages
  const [error, setError] = React.useState<string>();

  // Function to handle the API request to add a new module
  function request() {
    axios
      .post(`${API_ENDPOINT}/modules`, module)
      .then(() => {
        // Update the parent component and close the dialog on success
        props.update();
        props.onClose();
      })
      .catch((response) => {
        // Set the error message on failure
        setError(response.message);
      });
  }

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle>Add New Module</DialogTitle>
      <DialogContent>
        {error && (
          <Alert severity="error" sx={{ marginBottom: '20px' }}>
            {error}
          </Alert>
        )}
        <TextField
          fullWidth
          variant="outlined"
          label="Module Code"
          sx={{ marginBottom: '20px' }}
          onChange={(e) => {
            // Update the module code in uppercase
            setModule({ ...module, code: e.target.value.toUpperCase() });
          }}
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Module Name"
          sx={{ marginBottom: '20px' }}
          onChange={(e) => {
            // Update the module name
            setModule({ ...module, name: e.target.value });
          }}
        />
        <TextField
          fullWidth
          variant="outlined"
          type="number"
          label="Max Seats"
          sx={{ marginBottom: '20px' }}
          onChange={(e) => {
            // Update the max seats if the value is a positive number
            const val = parseInt(e.target.value, 10);
            if (!isNaN(val) && val > 0) {
              setModule({ ...module, maxSeats: val });
            }
          }}
        />
        <FormControlLabel
          control={
            <Switch
              checked={module.mnc ?? false}
              onChange={(e) => {
                // Update the MNC switch state
                setModule({ ...module, mnc: e.target.checked });
              }}
            />
          }
          label="MNC?"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={props.onClose}>Cancel</Button>
        <Button onClick={request} variant="contained">
          Add
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default AddModule;
