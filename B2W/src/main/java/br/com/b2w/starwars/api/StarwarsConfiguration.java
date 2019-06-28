package br.com.b2w.starwars.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class StarwarsConfiguration extends Configuration {

 private String accessKey;
 private String secretKey;

 @JsonProperty("accessKey")
  public String getAccessKey() {
    return accessKey;
  }

  @JsonProperty("accessKey")
  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  @JsonProperty("secretKey")
  public String getSecretKey() {
    return secretKey;
  }

  @JsonProperty("secretKey")
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
}
