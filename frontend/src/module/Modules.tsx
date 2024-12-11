import React from 'react';
import axios from 'axios';
import {
  Typography,
  Alert,
  Grid,
  IconButton,
  TextField,
  Button,
  Container,
  Card,
  CardContent,
  Divider,
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import App from '../App';
import { EntityModelModule } from '../api/entityModelModule';
import { API_ENDPOINT } from '../config';
import AddModule from './AddModule';
import EditModule from './EditModule';
import './Modules.css';
import Navbar from '../components/Navbar';

function Modules() {
  const [modules, setModules] = React.useState<EntityModelModule[]>([]);
  const [filteredModules, setFilteredModules] = React.useState<
    EntityModelModule[]
  >([]);
  const [error, setError] = React.useState<string>();
  const [searchTerm, setSearchTerm] = React.useState('');
  const [showAddModal, setShowAddModal] = React.useState(false);
  const [showEditModal, setShowEditModal] = React.useState(false);
  const [selectedModule, setSelectedModule] =
    React.useState<EntityModelModule>();

  React.useEffect(() => {
    updateModules();
  }, []);

  const filterModules = React.useCallback(() => {
    const term = searchTerm.toLowerCase();
    const filtered = modules.filter(
      (m) =>
        m.code?.toLowerCase().includes(term) ||
        m.name?.toLowerCase().includes(term)
    );
    setFilteredModules(filtered);
  }, [searchTerm, modules]);

  React.useEffect(() => {
    filterModules();
  }, [searchTerm, modules, filterModules]);

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

  function handleDelete(m: EntityModelModule) {
    if (m.code) {
      axios
        .delete(`${API_ENDPOINT}/modules/${m.code}`)
        .then(() => updateModules())
        .catch((res) => setError(res.message));
    }
  }

  function handleEdit(m: EntityModelModule) {
    setSelectedModule(m);
    setShowEditModal(true);
  }

  return (
    <>
      <Navbar currentPage="modules" />
      <App>
        <Container maxWidth="md" className="modules-container">
          <Card className="modules-card" elevation={2}>
            <CardContent>
              <Typography variant="h4" gutterBottom className="modules-title">
                Modules
              </Typography>
              <Typography
                variant="body2"
                sx={{ color: '#666', marginBottom: '30px', lineHeight: 1.5 }}
              >
                Here you can view, search, add, update, and delete course
                modules.
              </Typography>
              {error && (
                <Alert severity="error" sx={{ marginBottom: '20px' }}>
                  {error}
                </Alert>
              )}

              <div className="modules-actions">
                <TextField
                  variant="outlined"
                  size="small"
                  placeholder="Search by code or name..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="search-field"
                />
                <IconButton
                  onClick={updateModules}
                  className="action-button"
                  title="Refresh"
                >
                  <RefreshIcon />
                </IconButton>
                <Button
                  variant="contained"
                  startIcon={<AddIcon />}
                  onClick={() => setShowAddModal(true)}
                  className="action-button"
                >
                  Add New Module
                </Button>
              </div>

              <Divider sx={{ marginY: '30px' }} />

              {filteredModules.length < 1 && !error && (
                <Alert severity="warning">No modules found</Alert>
              )}

              {filteredModules.length > 0 && (
                <>
                  <Grid container className="modules-header-row">
                    <Grid item xs={2}>
                      Code
                    </Grid>
                    <Grid item xs={7}>
                      Name
                    </Grid>
                    <Grid item xs={1}>
                      MNC
                    </Grid>
                    <Grid item xs={2} className="action-column">
                      Actions
                    </Grid>
                  </Grid>
                  {filteredModules.map((m) => {
                    return (
                      <Grid container key={m.code} className="module-row">
                        <Grid item xs={2}>
                          {m.code}
                        </Grid>
                        <Grid item xs={7}>
                          {m.name}
                        </Grid>
                        <Grid item xs={1}>
                          {m.mnc ? 'Yes' : 'No'}
                        </Grid>
                        <Grid item xs={2} className="action-column">
                          <IconButton
                            onClick={() => handleEdit(m)}
                            title="Edit"
                            className="icon-btn"
                          >
                            <EditIcon />
                          </IconButton>
                          <IconButton
                            onClick={() => handleDelete(m)}
                            title="Delete"
                            className="icon-btn"
                          >
                            <DeleteIcon />
                          </IconButton>
                        </Grid>
                      </Grid>
                    );
                  })}
                </>
              )}
            </CardContent>
          </Card>
        </Container>

        {showAddModal && (
          <AddModule
            open={showAddModal}
            onClose={() => setShowAddModal(false)}
            update={updateModules}
          />
        )}

        {showEditModal && selectedModule && (
          <EditModule
            open={showEditModal}
            onClose={() => setShowEditModal(false)}
            module={selectedModule}
            update={updateModules}
          />
        )}
      </App>
    </>
  );
}

export default Modules;
