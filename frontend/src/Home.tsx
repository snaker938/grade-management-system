import {
  Breadcrumbs,
  Typography,
  List,
  ListItemButton,
  ListItemText,
} from "@mui/material";

import App from "./App";

function Home() {
  return (
    <App>
      <Breadcrumbs>
        <Typography sx={{ color: "text.primary" }}>Home</Typography>
      </Breadcrumbs>

      <List>
        <ListItemButton href="/modules">
          <ListItemText>Modules</ListItemText>
        </ListItemButton>
        <ListItemButton href="/students">
          <ListItemText>Students</ListItemText>
        </ListItemButton>
        <ListItemButton href="/grades">
          <ListItemText>Grades</ListItemText>
        </ListItemButton>
      </List>
    </App>
  );
}

export default Home;
