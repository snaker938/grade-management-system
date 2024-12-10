import { Container } from "@mui/material";

function App(props: { children: React.ReactNode }) {
  return <Container>{props.children}</Container>;
}

export default App;
