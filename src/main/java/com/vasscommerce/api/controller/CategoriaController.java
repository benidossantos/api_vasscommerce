package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.CategoriaDTO;
import com.vasscommerce.api.dto.ProdutoResponse;
import com.vasscommerce.api.entity.Categoria;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.mapper.ProdutoMapper;
import com.vasscommerce.api.repository.CategoriaRepository;
import com.vasscommerce.api.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * GET /categoria                    -> Listagem de todas as categorias cadastradas
     * GET /categoria?nome=<nomeCategoria> -> Busca de categoria por nome
     */
    @GetMapping
    public List<CategoriaDTO> listarOuBuscarPorNome(@RequestParam(required = false) String nome) {
        List<Categoria> categorias = (nome == null || nome.isBlank())
                ? categoriaRepository.findAll()
                : categoriaRepository.findByNomeContainingIgnoreCase(nome);

        return categorias.stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * GET /categoria/{idcategoria}/produto -> Listagem de todos os produtos da categoria especificada
     */
    @GetMapping("/{idcategoria}/produto")
    public List<ProdutoResponse> listarProdutosDaCategoria(@PathVariable Long idcategoria) {
        // Garante que a categoria existe antes de listar os produtos
        if (!categoriaRepository.existsById(idcategoria)) {
            throw new ResourceNotFoundException("Categoria nao encontrada com id " + idcategoria);
        }

        return produtoRepository.findByCategoriaId(idcategoria).stream()
                .map(ProdutoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
