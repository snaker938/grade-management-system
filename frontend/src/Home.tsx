import {
  Box,
  Typography,
  Button,
  Container,
  Card,
  CardContent,
  Grid,
  Divider,
  IconButton,
} from '@mui/material';
import SchoolIcon from '@mui/icons-material/School';
import PeopleIcon from '@mui/icons-material/People';
import GradingIcon from '@mui/icons-material/Grading';
import './Home.css';

function Home() {
  return (
    <Box className="home-root">
      {/* Custom Navbar */}
      <header className="navbar">
        <div className="navbar-content">
          <div className="navbar-title">Awesome University</div>
          <nav className="navbar-links">
            <a href="/modules" className="nav-link">
              Modules
            </a>
            <a href="/students" className="nav-link">
              Students
            </a>
            <a href="/grades" className="nav-link">
              Grades
            </a>
          </nav>
        </div>
      </header>

      <Container maxWidth="sm" className="main-card-container">
        <Card className="main-card" elevation={2}>
          <CardContent>
            <Typography variant="h4" gutterBottom className="main-title">
              Welcome to Awesome University
            </Typography>
            <Typography
              variant="body1"
              gutterBottom
              sx={{ marginBottom: '5px', color: '#444', lineHeight: 1.5 }}
            >
              A web-based student grade management application for Awesome
              University.
            </Typography>
            <Typography
              variant="body2"
              sx={{
                fontStyle: 'italic',
                color: '#777',
                marginTop: '20px',
                marginBottom: '30px',
                textAlign: 'center',
              }}
            >
              Developed by <strong>Group 43</strong>
            </Typography>
            <Typography
              variant="body2"
              sx={{ marginBottom: '40px', color: '#666', lineHeight: 1.7 }}
            >
              Manage course modules, enroll students, and record their grades
              with ease. Leverage this modern interface to streamline academic
              record keeping, ensuring accuracy and efficiency.
            </Typography>

            <Button variant="contained" href="/modules" className="primary-cta">
              Manage Now
            </Button>

            <Divider sx={{ marginY: '50px' }} />

            <Typography
              variant="h6"
              gutterBottom
              sx={{ marginBottom: '30px', color: '#333', fontWeight: 600 }}
            >
              Explore Features
            </Typography>

            <Grid
              container
              spacing={4}
              justifyContent="center"
              className="features-grid"
            >
              <Grid item xs={12} sm={4}>
                <Box className="feature-option">
                  <IconButton href="/modules" className="feature-icon-button">
                    <SchoolIcon className="feature-icon" />
                  </IconButton>
                  <Typography variant="body2" className="feature-text">
                    Modules
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Box className="feature-option">
                  <IconButton href="/students" className="feature-icon-button">
                    <PeopleIcon className="feature-icon" />
                  </IconButton>
                  <Typography variant="body2" className="feature-text">
                    Students
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Box className="feature-option">
                  <IconButton href="/grades" className="feature-icon-button">
                    <GradingIcon className="feature-icon" />
                  </IconButton>
                  <Typography variant="body2" className="feature-text">
                    Grades
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
}

export default Home;
