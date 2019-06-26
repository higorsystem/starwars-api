import br.com.b2w.starwars.api.resource.PlanetaResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class StarwarsApiApplication extends Application<StarwarsConfiguration> {
  private static final String BASE_URL = "/api/v1/*";

  public static void main(String[] args) throws Exception {
    new StarwarsApiApplication().run(args);
  }

  @Override
  public void run(StarwarsConfiguration configuration, Environment environment) {
    final PlanetaResource planetaResource = new PlanetaResource();
    var jersey = environment.jersey();

    jersey.setUrlPattern(BASE_URL);
    jersey.register(planetaResource);
  }
}
