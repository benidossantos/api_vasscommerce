package com.vasscommerce.api.repository;

import com.vasscommerce.api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}
