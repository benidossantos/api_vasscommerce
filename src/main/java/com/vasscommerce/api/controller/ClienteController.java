package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.CartaoDTO;
import com.vasscommerce.api.dto.ClienteDTO;
import com.vasscommerce.api.dto.EnderecoDTO;
import com.vasscommerce.api.dto.PedidoDTO;
import com.vasscommerce.api.entity.Cliente;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.repository.CartaoRepository;
import com.vasscommerce.api.repository.ClienteRepository;
import com.vasscommerce.api.repository.PedidoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final CartaoRepository cartaoRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteController(ClienteRepository clienteRepository,
                              CartaoRepository cartaoRepository,
                              PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.cartaoRepository = cartaoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * GET /cliente/{id} -> Visualizar dados do cliente
     */
    @GetMapping("/{id}")
    public ClienteDTO visualizarCliente(@PathVariable Long id) {
        Cliente cliente = buscarClienteOuFalhar(id);
        return new ClienteDTO(cliente);
    }

    /**
     * GET /cliente/{idcliente}/formas-de-pagamento -> Listar todas as formas de pagamento
     * (cartoes cadastrados pelo cliente)
     */
    @GetMapping("/{idcliente}/formas-de-pagamento")
    public List<CartaoDTO> listarFormasDePagamento(@PathVariable Long idcliente) {
        buscarClienteOuFalhar(idcliente);

        return cartaoRepository.findByTitularId(idcliente).stream()
                .map(CartaoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * GET /cliente/{idcliente}/endereco -> Busca pelos dados do endereco do cliente
     */
    @GetMapping("/{idcliente}/endereco")
    public EnderecoDTO buscarEnderecoDoCliente(@PathVariable Long idcliente) {
        Cliente cliente = buscarClienteOuFalhar(idcliente);

        if (cliente.getEndereco() == null) {
            throw new ResourceNotFoundException("Cliente " + idcliente + " nao possui endereco cadastrado");
        }

        return new EnderecoDTO(cliente.getEndereco());
    }

    /**
     * GET /cliente/{idcliente}/pedido -> Listagem de pedidos de um cliente
     */
    @GetMapping("/{idcliente}/pedido")
    public List<PedidoDTO> listarPedidosDoCliente(@PathVariable Long idcliente) {
        buscarClienteOuFalhar(idcliente);

        return pedidoRepository.findByClienteId(idcliente).stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
    }

    private Cliente buscarClienteOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado com id " + id));
    }
}
