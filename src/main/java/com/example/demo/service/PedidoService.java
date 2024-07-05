package com.example.demo.service;

import com.example.demo.domain.entity.Pedido;
import com.example.demo.domain.entity.enums.StatusPedido;
import com.example.demo.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(int id);

    void atualizaStatus(int id, StatusPedido statusPedido);
}
