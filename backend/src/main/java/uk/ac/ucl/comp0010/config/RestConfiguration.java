package uk.ac.ucl.comp0010.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;

/**
 * Configures the REST repository endpoints to expose entity IDs
 * for select domain classes.
 */
@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

  /**
   * Exposes the IDs of Student, Module, and Grade entities in the REST responses.
   * 
   * @param config the RepositoryRestConfiguration to modify
   * @param cors   the CorsRegistry for additional CORS mappings if needed
   */
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
    config.exposeIdsFor(Student.class, Module.class, Grade.class);
  }
}
