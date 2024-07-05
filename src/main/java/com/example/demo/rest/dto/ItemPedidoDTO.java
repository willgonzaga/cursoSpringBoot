package com.example.demo.rest.dto;

//{
//    "cliente": 1,
//    "total": 100,
//    "itens": [
//        {
//            "produto": 1,
//            "quantidade": 10
//        }
//    ]
//}

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemPedidoDTO {
    private int produto;
    private int quantidade;
}
