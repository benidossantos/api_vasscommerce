package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.TipoCartaoDTO;
import com.vasscommerce.api.entity.TipoCartao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tipo-cartao")
public class TipoCartaoController {

    /**
     * GET /tipo-cartao -> Listagem de todos os tipos de cartao
     */
    @GetMapping
    public List<TipoCartaoDTO> listarTiposDeCartao() {
        return Arrays.stream(TipoCartao.values())
                .map(TipoCartaoDTO::new)
                .collect(Collectors.toList());
    }
}
