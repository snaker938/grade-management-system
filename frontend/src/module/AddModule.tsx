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

function AddModule(props: {
  open: boolean;
  onClose: () => void;
  update: () => void;
}) {
  const [module, setModule] = React.useState<EntityModelModule>({});
  const [error, setError] = React.useState<string>();

  function request() {
    axios
      .post(`${API_ENDPOINT}/modules`, module)
      .then(() => {
        props.update();
        props.onClose();
      })
      .catch((response) => {
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
            setModule({ ...module, code: e.target.value.toUpperCase() });
          }}
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Module Name"
          sx={{ marginBottom: '20px' }}
          onChange={(e) => {
            setModule({ ...module, name: e.target.value });
          }}
        />
        <FormControlLabel
          control={
            <Switch
              checked={module.mnc ?? false}
              onChange={(e) => {
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
