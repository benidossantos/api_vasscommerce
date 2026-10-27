package com.vasscommerce.api.model;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.mapper.ProdutoMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementacao de ProdutoModel que guarda os produtos em uma lista em memoria.
 * Os dados sao perdidos quando a aplicacao reinicia.
 *
 * Ativa quando o profile "mem" esta ativo (spring.profiles.active=mem).
 */
@Service("produtoModelMemoria")
@Profile("mem")
public class ProdutoModelMemoria implements ProdutoModel {

    // CopyOnWriteArrayList: segura para acessos concorrentes de varias requisicoes
    private final List<Produto> banco = new CopyOnWriteArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public Produto criar(ProdutoCreateRequest req) {
        Long novoId = seq.incrementAndGet();
        Produto p = ProdutoMapper.toEntity(req, novoId);

        // Em memoria nao ha @PrePersist do JPA, entao as datas sao preenchidas aqui
        LocalDateTime agora = LocalDateTime.now();
        p.setDataCadastro(agora);
        p.setDataUltimaAtualizacao(agora);

        banco.add(p);
        return p;
    }

    @Override
    public Optional<Produto> obterPorId(Long id) {
        return banco.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst();
    }

    @Override
    public List<Produto> buscar(String nome, BigDecimal valorMinimo, BigDecimal valorMaximo) {
        return banco.stream()
                .filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> valorMinimo == null || p.getValorUnitario().compareTo(valorMinimo) >= 0)
                .filter(p -> valorMaximo == null || p.getValorUnitario().compareTo(valorMaximo) <= 0)
                .sorted(Comparator.comparing(Produto::getId))
                .toList();
    }
}
