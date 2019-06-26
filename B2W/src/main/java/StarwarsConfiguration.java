import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class StarwarsConfiguration extends Configuration {

  @JsonProperty private String mensagem;

  public String getMensagem() {
    return mensagem;
  }
}
