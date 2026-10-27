package com.vasscommerce.api.repository;

import com.vasscommerce.api.entity.Produto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // @EntityGraph carrega a categoria junto, evitando LazyInitializationException
    // ao montar o DTO fora da transacao (open-in-view=false)
    @EntityGraph(attributePaths = "categoria")
    List<Produto> findByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = "categoria")
    Optional<Produto> findWithCategoriaById(Long id);

    @Query("""
            select p from Produto p join fetch p.categoria
            where (:nome is null or lower(p.nome) like lower(concat('%', :nome, '%')))
              and (:valorMinimo is null or p.valorUnitario >= :valorMinimo)
              and (:valorMaximo is null or p.valorUnitario <= :valorMaximo)
            order by p.id
            """)
    List<Produto> buscar(@Param("nome") String nome,
                         @Param("valorMinimo") BigDecimal valorMinimo,
                         @Param("valorMaximo") BigDecimal valorMaximo);
}
