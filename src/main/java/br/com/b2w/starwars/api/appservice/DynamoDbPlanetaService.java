package br.com.b2w.starwars.api.appservice;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.dao.IDynamoDbDao;
import br.com.b2w.starwars.api.dao.impl.DynamoDbPlanetaDao;
import br.com.b2w.starwars.api.dto.PlanetaDto;
import br.com.b2w.starwars.api.model.Planeta;
import br.com.b2w.starwars.api.parser.PlanetaParser;
import br.com.b2w.starwars.api.util.ApiSwapi;

import java.util.List;

public class DynamoDbPlanetaService {
    private IDynamoDbDao<Planeta> dao;
    private ApiSwapi api;

    public DynamoDbPlanetaService(StarwarsConfiguration configuration) {
        dao = new DynamoDbPlanetaDao(configuration);
        api = new ApiSwapi();
    }

    public void deletarTabela() {
        dao.deletarTabela();
    }

    public void criarTabela() {
        dao.criarTabela();
    }

    public void incluirPlaneta(PlanetaDto dto) throws Exception {
        validarInclusao(dto);
        quantidadeAparicoesPlaneta(dto);
        var entidade = PlanetaParser.parser(dto);
        dao.incluir(entidade);
    }

    private void validarInclusao(PlanetaDto dto) throws Exception {
        var planetaExiste = dao.buscarPorNome(dto.getNome());
        if (planetaExiste != null){
            throw new Exception("O planeta já existe na base de dados.");
        }
    }

    private void quantidadeAparicoesPlaneta(PlanetaDto dto) throws Exception {
        var consumoSwapi = api.solicitarRequisicao(dto.getNome());
        var results = consumoSwapi.get("results");
        dto.setQuantidadeAparicoes(results.getAsJsonArray().get(0).getAsJsonObject().get("films").getAsJsonArray().size());
    }

    public PlanetaDto buscarPorId(String id) {
        return PlanetaParser.parser(dao.buscarPorId(id));
    }
    public PlanetaDto buscarPorNome(String nome){
        return PlanetaParser.parser(dao.buscarPorNome(nome));
    }
    public List<PlanetaDto> retornarTodos() {
        return PlanetaParser.parser(dao.retornarTodos());
    }

    public void deletarPorId(String id){
        dao.deletarPorId(id);
    }

}
