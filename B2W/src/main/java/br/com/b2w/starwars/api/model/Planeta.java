package br.com.b2w.starwars.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Planeta {

  private Integer id;
  private String nome;
  private String clima;
  private String terreno;
  private Integer QuantidadeAparicoes;

  public Planeta(
      Integer id, String nome, String clima, String terreno, Integer quantidadeAparicoes) {
    this.id = id;
    this.nome = nome;
    this.clima = clima;
    this.terreno = terreno;
    QuantidadeAparicoes = quantidadeAparicoes;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getClima() {
    return clima;
  }

  public void setClima(String clima) {
    this.clima = clima;
  }

  public String getTerreno() {
    return terreno;
  }

  public void setTerreno(String terreno) {
    this.terreno = terreno;
  }

  public Integer getQuantidadeAparicoes() {
    return QuantidadeAparicoes;
  }

  public void setQuantidadeAparicoes(Integer quantidadeAparicoes) {
    QuantidadeAparicoes = quantidadeAparicoes;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, true);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, true);
  }
}
