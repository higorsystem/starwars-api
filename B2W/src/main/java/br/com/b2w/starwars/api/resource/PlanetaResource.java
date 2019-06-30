package br.com.b2w.starwars.api.resource;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.appservice.DynamoDbPlanetaService;
import br.com.b2w.starwars.api.dto.PlanetaDto;

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
    public Response buscarPorId(@PathParam("id") String id) {
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
    public Response listarTodosPlanetas() {
        return Response.ok(service.retornarTodos()).build();
    }

    @GET
    public Response buscarPorNome(@QueryParam("nome") String nome) {
        try{
            var retorno = service.buscarPorNome(nome);

            if (retorno == null)
                return Response.status(Response.Status.NOT_FOUND).entity("O planeta: " + nome + " não foi encontrado.").build();

            return Response.ok(retorno).build();
        }catch(Exception ex){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @POST
    public Response adicionarPlaneta(PlanetaDto contrato) {
        try{
            service.incluirPlaneta(contrato);
            var contratoSalvo = service.buscarPorNome(contrato.getNome());
            return Response.status(Response.Status.CREATED).entity(contratoSalvo).build();
        }catch(Exception ex){
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluirPlanetaPorId(@PathParam("id") String id) {
        try{
            service.deletarPorId(id);
            return Response.ok("O planeta com id " + id + " foi removido com sucesso!!!").build();
        }catch(Exception ex){
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

    }
}
