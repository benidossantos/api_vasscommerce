package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.dto.ProdutoResponse;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.mapper.ProdutoMapper;
import com.vasscommerce.api.model.ProdutoModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * Controller de Produto. Depende apenas da interface ProdutoModel: nao sabe se
 * os dados estao em memoria ou em banco. A implementacao e injetada pelo Spring
 * pelo construtor, conforme o profile ativo (mem ou sql).
 */
@RestController
@RequestMapping(value = "/produto", produces = "application/json")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);

    private final ProdutoModel model;

    public ProdutoController(ProdutoModel model) {  // injecao por construtor
        this.model = model;
        // getUserClass ignora o proxy criado pelo Spring (ex.: por causa do @Transactional)
        log.info("ProdutoController usando a implementacao {}", ClassUtils.getUserClass(model).getSimpleName());
    }

    /**
     * POST /produto -> Cadastro de produto (201 Created + header Location)
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProdutoResponse> create(@Valid @RequestBody ProdutoCreateRequest body) {
        Produto created = model.criar(body);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(ProdutoMapper.toResponse(created));
    }

    /**
     * GET /produto/{id} -> Visualizar dados do produto (200 ou 404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> get(@PathVariable Long id) {
        return model.obterPorId(id)
                .map(p -> ResponseEntity.ok(ProdutoMapper.toResponse(p)))
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id " + id));
    }

    /**
     * GET /produto?nome=&valorMinimo=&valorMaximo= -> Busca de produtos (filtros opcionais)
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> list(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) BigDecimal valorMinimo,
            @RequestParam(required = false) BigDecimal valorMaximo) {

        List<Produto> found = model.buscar(nome, valorMinimo, valorMaximo);
        return ResponseEntity.ok(found.stream().map(ProdutoMapper::toResponse).toList());
    }
}
