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
  const [showInspectModal, setShowInspectModal] = React.useState(false);
  const [inspectModule, setInspectModule] = React.useState<EntityModelModule>();

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

  function handleInspect(m: EntityModelModule) {
    setInspectModule(m);
    setShowInspectModal(true);
  }

  return (
    <>
      <Navbar currentPage="modules" />
      <App>
        <Container maxWidth="md" className="modules-container" style={{paddingTop: '140px'}}>
          <Card className="modules-card" elevation={2}>
            <CardContent>
              <Typography variant="h4" gutterBottom className="modules-title">
                Modules
              </Typography>
              <Typography
                variant="body2"
                sx={{ color: '#666', marginBottom: '30px', lineHeight: 1.5 }}
              >
                Here you can view, search, add, update, and delete course modules. You can also inspect a module to manage its enrolled students, assign grades, and update its capacity.
              </Typography>
              {error && (
                <Alert severity="error" sx={{ marginBottom: '20px' }}>
                  {error}
                </Alert>
              )}

              <div className="modules-actions">
                <div className="left-actions">
                  <IconButton
                    onClick={updateModules}
                    className="action-button-icon"
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
                <div className="right-actions">
                  <TextField
                    variant="outlined"
                    size="small"
                    placeholder="Search by code or name..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-field"
                  />
                </div>
              </div>

              <Divider sx={{ marginY: '30px' }} />

              {filteredModules.length < 1 && !error && (
                <Alert severity="warning">No modules found</Alert>
              )}

              {filteredModules.length > 0 && (
                <div className="modules-list-container">
                  <Grid container className="modules-header-row">
                    <Grid item xs={2}>
                      Code
                    </Grid>
                    <Grid item xs={2}>
                      Name
                    </Grid>
                    <Grid item xs={2}>
                      MNC
                    </Grid>
                    <Grid item xs={2}>
                      Enrolled/Max
                    </Grid>
                    <Grid item xs={2}>
                      Actions
                    </Grid>
                    <Grid item xs={2}>
                      Inspect
                    </Grid>
                  </Grid>
                  <div className="modules-scrollable">
                    {filteredModules.map((m) => (
                      <Grid container key={m.code} className="module-row">
                        <Grid item xs={2}>
                          {m.code}
                        </Grid>
                        <Grid item xs={2}>
                          {m.name}
                        </Grid>
                        <Grid item xs={2}>
                          {m.mnc ? 'Yes' : 'No'}
                        </Grid>
                        <Grid item xs={2}>
                          {m.enrolledCount ?? 0}/{m.maxSeats ?? 0}
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
                        <Grid item xs={2} className="action-column">
                          <IconButton
                            onClick={() => handleInspect(m)}
                            title="Inspect"
                            className="icon-btn"
                          >
                            <SearchIcon />
                          </IconButton>
                        </Grid>
                      </Grid>
                    ))}
                  </div>
                </div>
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
