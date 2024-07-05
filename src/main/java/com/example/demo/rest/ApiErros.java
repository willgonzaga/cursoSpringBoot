package com.example.demo.rest;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiErros {
    public List<String> erros;

    public ApiErros(String mensagemErro) {
        this.erros = Arrays.asList(mensagemErro);
    }

    public ApiErros(List<String> erros) {
        this.erros = erros;
    }
}
