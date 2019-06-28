package br.com.b2w.starwars.api.appservice;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.dao.IDynamoDbDao;
import br.com.b2w.starwars.api.dao.impl.DynamoDbPlanetaDao;
import br.com.b2w.starwars.api.dto.PlanetaDto;
import br.com.b2w.starwars.api.model.Planeta;
import br.com.b2w.starwars.api.parser.PlanetaParser;

import java.util.List;

public class DynamoDbPlanetaService {
    private IDynamoDbDao<Planeta> dao;

    public DynamoDbPlanetaService(StarwarsConfiguration configuration) {
        dao = new DynamoDbPlanetaDao(configuration);
    }

    public void deletarTabela() {
        dao.deletarTabela();
    }

    public void criarTabela() {
        dao.criarTabela();
    }

    public void incluirPlaneta(PlanetaDto dto) {
        var entidade = PlanetaParser.parser(dto);
        dao.incluir(entidade);
    }

    public PlanetaDto buscarPorId(int id) {
        return PlanetaParser.parser(dao.buscarPorId(id));
    }

    public List<PlanetaDto> retornarTodos() {
        return PlanetaParser.parser(dao.retornarTodos());
    }
}
