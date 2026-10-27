package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.ProdutoDTO;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * GET /produto/{id} -> Visualizar dados do produto
     */
    @GetMapping("/{id}")
    public ProdutoDTO visualizarProduto(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id " + id));
        return new ProdutoDTO(produto);
    }
}
