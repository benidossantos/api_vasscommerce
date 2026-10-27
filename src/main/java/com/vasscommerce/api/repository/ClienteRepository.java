package com.vasscommerce.api.repository;

import com.vasscommerce.api.entity.Cliente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @EntityGraph(attributePaths = {"endereco", "endereco.cidade", "endereco.cidade.estado"})
    Optional<Cliente> findWithEnderecoById(Long id);
}
