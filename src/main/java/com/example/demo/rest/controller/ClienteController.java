package com.example.demo.rest.controller;

import com.example.demo.domain.entity.Cliente;
import com.example.demo.domain.repository.Clientes;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/clientes")
@Api("Api de Clientes")
public class ClienteController {

    private Clientes repository;

    public ClienteController(Clientes repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado")
    })
    public Cliente getClienteById(@PathVariable @ApiParam("Id do cliente") int id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
                );
    }

    @PostMapping("/save")
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return repository.save(cliente);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Excluir um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente excluido com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado")
    })
    public void delete(@PathVariable int id) {
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return cliente;
                }).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
                );
    }

    @PutMapping("/atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid Cliente cliente) {
        repository.findById(id).map(clienteExistente -> {
            cliente.setId(clienteExistente.getId());
            repository.save(cliente);
            return clienteExistente;
        }).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado")
        );
    }

    @GetMapping
    @ApiOperation("Obter detalhes de varios clientes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado")
    })
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnorePaths("id")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Cliente> example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }
}
