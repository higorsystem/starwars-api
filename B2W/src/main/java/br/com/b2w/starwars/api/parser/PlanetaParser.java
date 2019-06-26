package br.com.b2w.starwars.api.parser;

import br.com.b2w.starwars.api.dto.PlanetaDto;
import br.com.b2w.starwars.api.model.Planeta;

import java.util.List;
import java.util.stream.Collectors;

public class PlanetaParser {

    public PlanetaParser() {
    }

    public static PlanetaDto parser(Planeta planeta) {
        if (planeta == null) {
            return null;
        }

        return new PlanetaDto(
                planeta.getId(),
                planeta.getNome(),
                planeta.getClima(),
                planeta.getTerreno(),
                planeta.getQuantidadeAparicoes());
    }

    public static List<PlanetaDto> parser(List<Planeta> planetas){
        if (planetas == null)
            return null;

        return planetas.stream().map(PlanetaParser::parser).collect(Collectors.toList());
    }
}
