# API do VassCommerce — Java + Spring Boot

Implementação em **Java 17 + Spring Boot 3.2** das rotas de leitura (GET)
descritas no enunciado do laboratório, a partir do diagrama de classes do
VassCommerce.

## Stack utilizada

- Java 17
- Spring Boot 3.2.5 (Web + Data JPA)
- Banco de dados **H2** em memória (recriado e populado a cada execução)
- Maven

## Como executar

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080` (o enunciado considera a porta
80, mas para não exigir privilégios administrativos em ambiente local usamos
a porta padrão do Spring Boot — altere `server.port` em
`application.properties` se quiser rodar exatamente na porta 80).

Ao iniciar, a classe `DataSeeder` popula o banco automaticamente com dados
de exemplo (estados, cidades, categorias, produtos, um cliente com endereço,
cartões e um pedido), para que todas as rotas possam ser testadas de
imediato.

Console do H2 (opcional, útil para inspecionar os dados): 
`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:vasscommerce`,
usuário `sa`, senha em branco.

## Rotas implementadas

| Método | Rota | Descrição |
|---|---|---|
| GET | `/categoria` | Listagem de todas as categorias cadastradas |
| GET | `/categoria?nome=<nomeCategoria>` | Busca de categoria por nome |
| GET | `/categoria/{idcategoria}/produto` | Listagem de todos os produtos da categoria especificada |
| GET | `/produto/{id}` | Visualizar dados do produto |
| GET | `/cliente/{id}` | Visualizar dados do cliente |
| GET | `/cliente/{idcliente}/formas-de-pagamento` | Listar todas as formas de pagamento (cartões) do cliente |
| GET | `/cliente/{idcliente}/endereco` | Busca pelos dados do endereço do cliente |
| GET | `/tipo-cartao` | Listagem de todos os tipos de cartão |
| GET | `/estado` | Listagem de todos os estados |
| GET | `/estado/{idestado}/cidade` | Listagem de todas as cidades de um estado |
| GET | `/cliente/{idcliente}/pedido` | Listagem de pedidos de um cliente |

Todas as rotas respondem em `application/json`. Se um recurso não for
encontrado, a API responde `404 Not Found` com um corpo JSON padronizado
(veja `GlobalExceptionHandler`).

## Exemplos de teste (com o cliente/produto/categoria/estado de id = 1)

```bash
curl http://localhost:8080/categoria
curl "http://localhost:8080/categoria?nome=eletro"
curl http://localhost:8080/categoria/1/produto
curl http://localhost:8080/produto/1
curl http://localhost:8080/cliente/1
curl http://localhost:8080/cliente/1/formas-de-pagamento
curl http://localhost:8080/cliente/1/endereco
curl http://localhost:8080/tipo-cartao
curl http://localhost:8080/estado
curl http://localhost:8080/estado/1/cidade
curl http://localhost:8080/cliente/1/pedido
```

## Estrutura do projeto

```
src/main/java/com/vasscommerce/api/
├── VasscommerceApiApplication.java   # classe principal
├── entity/                           # entidades JPA (mapeiam o diagrama de classes)
├── repository/                       # interfaces Spring Data JPA
├── dto/                              # objetos de transferência retornados pela API
├── controller/                       # controllers REST (uma rota por endpoint do enunciado)
├── exception/                        # tratamento de erros (404, etc.)
└── config/
    └── DataSeeder.java               # popula o banco H2 com dados de exemplo
```

## Modelagem — observações

- O enunciado deste laboratório pede apenas rotas de **leitura (GET)**.
  Por isso, entidades do diagrama que só seriam necessárias para as
  operações de escrita descritas em outra etapa do laboratório
  (`Administrador`, `TabelaPromocao`, `PromocaoItem`, `Pagamento`) não
  foram implementadas aqui, para manter o escopo alinhado ao que foi
  solicitado. A estrutura já está organizada (entidades + repositórios +
  DTOs + controllers separados) para que essas classes e as rotas de
  `POST`/`PUT`/`DELETE` possam ser adicionadas depois seguindo o mesmo
  padrão.
- `Cliente` reúne os atributos de `Usuario` e `Cliente` do diagrama (o
  Cliente é o Usuário que se cadastra na loja).
- `TipoCartao` e `PedidoStatus` foram implementados como `enum` do Java,
  refletindo as classes de enumeração do diagrama.
- `/cliente/{idcliente}/formas-de-pagamento` retorna os cartões (`Cartao`)
  cadastrados pelo cliente, que representam as formas de pagamento do
  diagrama.
