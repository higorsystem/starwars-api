package br.com.b2w.starwars.api.resource;

import br.com.b2w.starwars.api.dto.PlanetaDto;
import br.com.b2w.starwars.api.model.Planeta;
import br.com.b2w.starwars.api.parser.PlanetaParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/planeta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanetaResource {

  @GET
  @Path("/{id}")
  public Response buscarPorId(@PathParam("id") int id) {
    // TODO terminar essa implementação esse mock de id é um teste.
    if (2 == id) {
      Planeta planeta = new Planeta(2, "Alderaan", "temperate", "grasslands, mountains", 3);

      return Response.ok(new PlanetaParser().parser(planeta)).build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }

  @GET
  @Path("/listar-todos")
  public Response listarTodosPlanetas() throws Exception{
    var listaPlanetas = new ArrayList<Planeta>();
    Planeta planeta = new Planeta(2, "Alderaan", "temperate, tropical", "grasslands, mountains", 3);
    Planeta planeta2 = new Planeta(3, "Yavin IV", "temperate, tropical", "jungle, rainforests", 3);
    listaPlanetas.add(planeta);
    listaPlanetas.add(planeta2);

    return Response.ok(PlanetaParser.parser(listaPlanetas)).build();
  }

  @GET
  public Response buscarPorNome(@QueryParam("nome") String nome) throws Exception{
    // TODO terminar essa implementação esse mock de id é um teste.
    if (nome.equals("Alderaan")) {
      Planeta planeta = new Planeta(2, "Alderaan", "temperate", "grasslands, mountains", 3);

      return Response.ok(new PlanetaParser().parser(planeta)).build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }

  @POST
  public Response adicionarPlaneta(PlanetaDto contrato){
    return Response.status(Response.Status.CREATED).entity("Gravado com sucesso!!!").build();
  }

  @DELETE
  @Path("/{id}")
  public Response excluirPlanetaPorId(@PathParam("id") int id){
    return Response.ok("O planeta com id " + id + " foi removido com sucesso!!!").build();
  }
}
