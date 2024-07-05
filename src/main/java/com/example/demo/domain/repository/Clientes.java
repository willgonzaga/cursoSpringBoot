package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface Clientes extends JpaRepository<Cliente, Integer> {

    @Query(value = "select * from cliente where nome like %:nome%", nativeQuery = true)
    List<Cliente> encontrarPorNome(@Param("nome") String nome);

    boolean existsByNome(String nome);

    @Modifying
    @Query(" delete from Cliente c where c.nome =:nome ")
    void deleteByNome(@Param("nome") String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = :id")
    Cliente findClienteFetchPedidos(@Param("id") int id);
}
