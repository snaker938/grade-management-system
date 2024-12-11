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

// Component to edit a module
function EditModule(props: {
  open: boolean;
  onClose: () => void;
  update: () => void;
  module: EntityModelModule;
}) {
  // State to manage the module being edited
  const [module, setModule] = React.useState<EntityModelModule>(props.module);
  // State to manage any error messages
  const [error, setError] = React.useState<string>();

  // Effect to update the module state when props.module changes
  React.useEffect(() => {
    setModule(props.module);
  }, [props.module]);

  // Function to save changes to the module
  function saveChanges() {
    if (module.code) {
      axios
        .put(`${API_ENDPOINT}/modules/${module.code}`, module)
        .then(() => {
          props.update();
          props.onClose();
        })
        .catch((res) => setError(res.message));
    }
  }

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle>Edit Module</DialogTitle>
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
          value={module.code ?? ''}
          disabled
          sx={{ marginBottom: '20px' }}
        />
        <TextField
          fullWidth
          variant="outlined"
          label="Module Name"
          value={module.name ?? ''}
          onChange={(e) => {
            setModule({ ...module, name: e.target.value });
          }}
          sx={{ marginBottom: '20px' }}
        />
        <TextField
          fullWidth
          variant="outlined"
          type="number"
          label="Max Seats"
          sx={{ marginBottom: '20px' }}
          onChange={(e) => {
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
                setModule({ ...module, mnc: e.target.checked });
              }}
            />
          }
          label="MNC?"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={props.onClose}>Cancel</Button>
        <Button onClick={saveChanges} variant="contained">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default EditModule;
