package com.example.demo.rest.controller;

import com.example.demo.domain.entity.Cliente;
import com.example.demo.domain.repository.Clientes;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private Clientes repository;

    public ClienteController(Clientes repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable int id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
                );
    }

    @PostMapping("/save")
    @ResponseStatus(CREATED)
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return repository.save(cliente);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(NO_CONTENT)
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
