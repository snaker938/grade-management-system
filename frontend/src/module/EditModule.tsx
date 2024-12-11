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

function EditModule(props: {
  open: boolean;
  onClose: () => void;
  update: () => void;
  module: EntityModelModule;
}) {
  const [module, setModule] = React.useState<EntityModelModule>(props.module);
  const [error, setError] = React.useState<string>();

  React.useEffect(() => {
    setModule(props.module);
  }, [props.module]);

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
