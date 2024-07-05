package com.example.demo.rest.controller;

import com.example.demo.domain.entity.ItemPedido;
import com.example.demo.domain.entity.Pedido;
import com.example.demo.domain.entity.enums.StatusPedido;
import com.example.demo.rest.dto.AtualizacaoStatusPedidoDTO;
import com.example.demo.rest.dto.InformacaoItemPedidoDTO;
import com.example.demo.rest.dto.InformacoesPedidoDTO;
import com.example.demo.rest.dto.PedidoDTO;
import com.example.demo.service.PedidoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public int save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("/{id}")
    public InformacoesPedidoDTO getById(@PathVariable int id) {
        return service
                .obterPedidoCompleto(id)
                .map(p -> converter(p) )
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable int id, @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String statusPedido = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(statusPedido));

    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }



}
