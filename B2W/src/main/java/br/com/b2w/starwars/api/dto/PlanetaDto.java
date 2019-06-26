package br.com.b2w.starwars.api.dto;

public class PlanetaDto {
  private Integer id;
  private String nome;
  private String clima;
  private String terreno;
  private Integer QuantidadeAparicoes;

  public PlanetaDto(
      Integer id, String nome, String clima, String terreno, Integer quantidadeAparicoes) {
    this.id = id;
    this.nome = nome;
    this.clima = clima;
    this.terreno = terreno;
    QuantidadeAparicoes = quantidadeAparicoes;
  }

  public PlanetaDto(){
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
}
