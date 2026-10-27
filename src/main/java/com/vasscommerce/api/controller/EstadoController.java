package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.CidadeDTO;
import com.vasscommerce.api.dto.EstadoDTO;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.repository.CidadeRepository;
import com.vasscommerce.api.repository.EstadoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estado")
public class EstadoController {

    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;

    public EstadoController(EstadoRepository estadoRepository, CidadeRepository cidadeRepository) {
        this.estadoRepository = estadoRepository;
        this.cidadeRepository = cidadeRepository;
    }

    /**
     * GET /estado -> Listagem de todos os estados
     */
    @GetMapping
    public List<EstadoDTO> listarEstados() {
        return estadoRepository.findAll().stream()
                .map(EstadoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * GET /estado/{idestado}/cidade -> Listagem de todas as cidades de um estado
     */
    @GetMapping("/{idestado}/cidade")
    public List<CidadeDTO> listarCidadesDoEstado(@PathVariable Long idestado) {
        if (!estadoRepository.existsById(idestado)) {
            throw new ResourceNotFoundException("Estado nao encontrado com id " + idestado);
        }

        return cidadeRepository.findByEstadoId(idestado).stream()
                .map(CidadeDTO::new)
                .collect(Collectors.toList());
    }
}
