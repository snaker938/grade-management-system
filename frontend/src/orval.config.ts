import { defineConfig } from "orval";
import { API_ENDPOINT } from "./config";

export default defineConfig({
  evo: {
    output: {
      mode: "tags",
      schemas: "api",
      mock: false, // enable/disable test mock generation
      // I recommend enabling this option if you generate an api client
      prettier: true, // recommended if you use prettier
      clean: true, // recreate the whole folder (avoid outdated files)
    },
    input: {
      // use your own Swagger url: http://server:port/context-path/v3/api-docs
      target: `${API_ENDPOINT}/api-docs`,
    },
  },
});
