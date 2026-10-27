package com.vasscommerce.api.config;

import com.vasscommerce.api.entity.*;
import com.vasscommerce.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Popula o banco H2 em memoria com dados de exemplo toda vez que a aplicacao
 * sobe, para que as rotas da API possam ser testadas imediatamente.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final CartaoRepository cartaoRepository;
    private final PedidoRepository pedidoRepository;

    public DataSeeder(EstadoRepository estadoRepository,
                       CidadeRepository cidadeRepository,
                       CategoriaRepository categoriaRepository,
                       ProdutoRepository produtoRepository,
                       ClienteRepository clienteRepository,
                       CartaoRepository cartaoRepository,
                       PedidoRepository pedidoRepository) {
        this.estadoRepository = estadoRepository;
        this.cidadeRepository = cidadeRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.cartaoRepository = cartaoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void run(String... args) {

        // ---------- Estados e Cidades ----------
        Estado rj = estadoRepository.save(new Estado("RJ", "Rio de Janeiro"));
        Estado sp = estadoRepository.save(new Estado("SP", "Sao Paulo"));

        Cidade voltaRedonda = cidadeRepository.save(new Cidade("Volta Redonda", rj));
        cidadeRepository.save(new Cidade("Barra Mansa", rj));
        cidadeRepository.save(new Cidade("Resende", rj));
        cidadeRepository.save(new Cidade("Sao Paulo", sp));
        cidadeRepository.save(new Cidade("Campinas", sp));

        // ---------- Categorias ----------
        Categoria eletronicos = categoriaRepository.save(new Categoria(
                "Eletronicos",
                "Produtos eletronicos em geral",
                "https://vasscommerce.com.br/img/categorias/eletronicos.png"));

        Categoria livros = categoriaRepository.save(new Categoria(
                "Livros",
                "Livros fisicos e digitais",
                "https://vasscommerce.com.br/img/categorias/livros.png"));

        // ---------- Produtos ----------
        Produto smartphone = new Produto();
        smartphone.setNome("Smartphone XZ200");
        smartphone.setDescricao("Smartphone com 128GB de armazenamento");
        smartphone.setFotoUrl("https://vasscommerce.com.br/img/produtos/xz200.png");
        smartphone.setValorUnitario(new BigDecimal("1899.90"));
        smartphone.setCategoria(eletronicos);
        produtoRepository.save(smartphone);

        Produto fone = new Produto();
        fone.setNome("Fone de Ouvido Bluetooth");
        fone.setDescricao("Fone sem fio com cancelamento de ruido");
        fone.setFotoUrl("https://vasscommerce.com.br/img/produtos/fone-bt.png");
        fone.setValorUnitario(new BigDecimal("299.90"));
        fone.setCategoria(eletronicos);
        produtoRepository.save(fone);

        Produto livro = new Produto();
        livro.setNome("Clean Code");
        livro.setDescricao("Livro sobre boas praticas de programacao");
        livro.setFotoUrl("https://vasscommerce.com.br/img/produtos/clean-code.png");
        livro.setValorUnitario(new BigDecimal("89.90"));
        livro.setCategoria(livros);
        produtoRepository.save(livro);

        // ---------- Cliente + Endereco ----------
        Endereco endereco = new Endereco();
        endereco.setRua("Rua das Palmeiras");
        endereco.setNumero(120);
        endereco.setCep("27275-000");
        endereco.setComplemento("Apto 302");
        endereco.setTelefone("(24) 99988-7766");
        endereco.setBairro("Jardim Paraiba");
        endereco.setCidade(voltaRedonda);

        Cliente maria = new Cliente();
        maria.setNomeCompleto("Maria da Silva");
        maria.setEmail("maria.silva@email.com");
        maria.setSenha("senha-criptografada");
        maria.setFotoUrl("https://vasscommerce.com.br/img/clientes/10.png");
        maria.setDataNascimento(LocalDate.of(1995, 6, 20));
        maria.setCpf("123.456.789-00");
        maria.setEndereco(endereco);
        clienteRepository.save(maria);

        // ---------- Cartoes (formas de pagamento) ----------
        Cartao cartaoCredito = new Cartao();
        cartaoCredito.setTipo(TipoCartao.CREDITO);
        cartaoCredito.setExcluido(false);
        cartaoCredito.setTitular(maria);
        cartaoRepository.save(cartaoCredito);

        Cartao cartaoDebito = new Cartao();
        cartaoDebito.setTipo(TipoCartao.DEBITO);
        cartaoDebito.setExcluido(false);
        cartaoDebito.setTitular(maria);
        cartaoRepository.save(cartaoDebito);

        // ---------- Pedido ----------
        Pedido pedido = new Pedido();
        pedido.setCliente(maria);
        pedido.setStatusAtual(PedidoStatus.AGUARDANDO_PAGAMENTO);
        pedido.setValorTotal(new BigDecimal("2499.70"));

        ItemPedido itemSmartphone = new ItemPedido();
        itemSmartphone.setPedido(pedido);
        itemSmartphone.setProduto(smartphone);
        itemSmartphone.setQuantidade(1);
        itemSmartphone.setValorUnitario(smartphone.getValorUnitario());

        ItemPedido itemFone = new ItemPedido();
        itemFone.setPedido(pedido);
        itemFone.setProduto(fone);
        itemFone.setQuantidade(2);
        itemFone.setValorUnitario(fone.getValorUnitario());

        pedido.getItens().add(itemSmartphone);
        pedido.getItens().add(itemFone);

        pedidoRepository.save(pedido);
    }
}
