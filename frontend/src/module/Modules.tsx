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
import SearchIcon from '@mui/icons-material/Search';

import App from '../App';
import { EntityModelModule } from '../api/entityModelModule';
import { API_ENDPOINT } from '../config';
import AddModule from './AddModule';
import EditModule from './EditModule';
import InspectModule from './InspectModule';
import Navbar from '../components/Navbar';

function Modules() {
  // State to store the list of modules
  const [modules, setModules] = React.useState<EntityModelModule[]>([]);
  // State to store the filtered list of modules based on search term
  const [filteredModules, setFilteredModules] = React.useState<
    EntityModelModule[]
  >([]);
  // State to store any error messages
  const [error, setError] = React.useState<string>();
  // State to store the search term
  const [searchTerm, setSearchTerm] = React.useState('');
  // State to control the visibility of the Add Module modal
  const [showAddModal, setShowAddModal] = React.useState(false);
  // State to control the visibility of the Edit Module modal
  const [showEditModal, setShowEditModal] = React.useState(false);
  // State to store the selected module for editing
  const [selectedModule, setSelectedModule] =
    React.useState<EntityModelModule>();
  // State to control the visibility of the Inspect Module modal
  const [showInspectModal, setShowInspectModal] = React.useState(false);
  // State to store the module to be inspected
  const [inspectModule, setInspectModule] = React.useState<EntityModelModule>();

  // Effect to fetch and update the list of modules when the component mounts
  React.useEffect(() => {
    updateModules();
  }, []);

  // Function to filter the modules based on the search term
  const filterModules = React.useCallback(() => {
    const term = searchTerm.toLowerCase();
    const filtered = modules.filter(
      (m) =>
        m.code?.toLowerCase().includes(term) ||
        m.name?.toLowerCase().includes(term)
    );
    setFilteredModules(filtered);
  }, [searchTerm, modules]);

  // Effect to filter the modules whenever the search term or modules list changes
  React.useEffect(() => {
    filterModules();
  }, [searchTerm, modules, filterModules]);

  // Function to fetch the list of modules from the API
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

  // Function to handle the deletion of a module
  function handleDelete(m: EntityModelModule) {
    if (m.code) {
      axios
        .delete(`${API_ENDPOINT}/modules/${m.code}`)
        .then(() => updateModules())
        .catch((res) => setError(res.message));
    }
  }

  // Function to handle the editing of a module
  function handleEdit(m: EntityModelModule) {
    setSelectedModule(m);
    setShowEditModal(true);
  }

  // Function to handle the inspection of a module
  function handleInspect(m: EntityModelModule) {
    setInspectModule(m);
    setShowInspectModal(true);
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
                modules. You can also inspect a module to manage its enrolled
                students, assign grades, and update its capacity.
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
                    <Grid item xs={5}>
                      Name
                    </Grid>
                    <Grid item xs={1}>
                      MNC
                    </Grid>
                    <Grid item xs={2}>
                      Enrolled/Max
                    </Grid>
                    <Grid item xs={1} className="action-column">
                      Inspect
                    </Grid>
                    <Grid item xs={1} className="action-column">
                      Actions
                    </Grid>
                  </Grid>
                  {filteredModules.map((m) => {
                    return (
                      <Grid container key={m.code} className="module-row">
                        <Grid item xs={2}>
                          {m.code}
                        </Grid>
                        <Grid item xs={5}>
                          {m.name}
                        </Grid>
                        <Grid item xs={1}>
                          {m.mnc ? 'Yes' : 'No'}
                        </Grid>
                        <Grid item xs={2}>
                          {m.enrolledCount ?? 0}/{m.maxSeats ?? 0}
                        </Grid>
                        <Grid item xs={1} className="action-column">
                          <IconButton
                            onClick={() => handleInspect(m)}
                            title="Inspect"
                            className="icon-btn"
                          >
                            <SearchIcon />
                          </IconButton>
                        </Grid>
                        <Grid item xs={1} className="action-column">
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

        {showInspectModal && inspectModule && (
          <InspectModule
            open={showInspectModal}
            onClose={() => setShowInspectModal(false)}
            module={inspectModule}
            update={updateModules}
          />
        )}
      </App>
    </>
  );
}

export default Modules;
