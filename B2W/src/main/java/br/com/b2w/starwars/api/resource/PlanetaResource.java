package br.com.b2w.starwars.api.resource;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.appservice.DynamoDbPlanetaService;
import br.com.b2w.starwars.api.dto.PlanetaDto;
import br.com.b2w.starwars.api.model.Planeta;
import br.com.b2w.starwars.api.parser.PlanetaParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/planeta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanetaResource {

    private DynamoDbPlanetaService service;

    public PlanetaResource(StarwarsConfiguration configuration) {
        service = new DynamoDbPlanetaService(configuration);
        service.criarTabela();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
         var objPlaneta = service.buscarPorId(id);
         return Response.ok(objPlaneta).build();
    }

    @GET
    @Path("/deletar-tabela")
    public Response deletarTabela(){
        service.deletarTabela();
        return Response.ok("Tabela excluida com sucesso!").build();
    }

    @GET
    @Path("/listar-todos")
    public Response listarTodosPlanetas() throws Exception {
        return Response.ok(service.retornarTodos()).build();
    }

    @GET
    public Response buscarPorNome(@QueryParam("nome") String nome) throws Exception {
        // TODO terminar essa implementação esse mock de id é um teste.
        if (nome.equals("Alderaan")) {
            Planeta planeta = new Planeta(2, "Alderaan", "temperate", "grasslands, mountains", 3);

            return Response.ok(new PlanetaParser().parser(planeta)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response adicionarPlaneta(PlanetaDto contrato) {
        try{
            service.incluirPlaneta(contrato);
            return Response.status(Response.Status.CREATED).entity("Gravado com sucesso!!!").build();
        }catch(Exception ex){
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluirPlanetaPorId(@PathParam("id") int id) {
        return Response.ok("O planeta com id " + id + " foi removido com sucesso!!!").build();
    }
}
