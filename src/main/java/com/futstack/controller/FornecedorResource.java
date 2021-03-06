package com.futstack.controller;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.futstack.Model.Fornecedor;
import com.futstack.repository.MovimentacaoRepository;

@Path("/fornecedor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FornecedorResource {
    @Inject
    MovimentacaoRepository movimentacaoRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fornecedor> listarTodos() {
        try {
            return Fornecedor.listAll();
        } catch (Exception e) {
            throw new NotFoundException("Não consegui consultar...");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response criar(Fornecedor dto) {
        try {
            dto.persist();
            movimentacaoRepository.registraCriacaoFornecedor(dto);
            Response retorno = Response.status(Response.Status.CREATED).build();
            return retorno;
        }catch (Exception e){
            throw new NotFoundException(e);
        }
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public String alterar(@PathParam("id") int id, Fornecedor fornecedor) {
        try {
            Optional<Fornecedor> fornecedorOp = Fornecedor.findByIdOptional(id);
            if (fornecedorOp.isEmpty()) {
                throw new NotFoundException();
            }
            Fornecedor dto = fornecedorOp.get();


            dto.setFo_nome(fornecedor.getFo_nome());
            movimentacaoRepository.registraAlteracaoFornecedor(dto);
            dto.persist();

            return "Alteração feita com sucesso!";
        } catch (Exception e) {
            throw new NotFoundException(e);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public String exclui(@PathParam("id") int id){
        try {
            
        Optional<Fornecedor> fornecedorOp = Fornecedor.findByIdOptional(id);
        Fornecedor dto = fornecedorOp.get();
        fornecedorOp.ifPresentOrElse(Fornecedor::delete, () -> {throw new NotFoundException();});
        movimentacaoRepository.registraExclusaoFornecedor(dto);
        return "Fornecedor excluido com sucesso!!";
        } catch (Exception e) {
            throw new NotFoundException(e);
        }
    }
}
