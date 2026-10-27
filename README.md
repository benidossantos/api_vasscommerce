# API do VassCommerce — Java + Spring Boot

Implementação em **Java 17 + Spring Boot 3.2** da API do VassCommerce, a
partir do diagrama de classes do laboratório.

**Etapa atual — IoC/DI (Controllers x Models):** o `ProdutoController` depende
apenas da interface `ProdutoModel`; as implementações concretas
(`ProdutoModelMemoria` e `ProdutoModelSql`) são beans `@Service` escolhidos
pelo Spring via `@Profile` e injetados pelo construtor. Veja a seção
[IoC/DI — ProdutoModel](#iocdi--produtomodel) e o [`CHECKLIST.md`](CHECKLIST.md).

## Stack utilizada

- Java 17
- Spring Boot 3.2.5 (Web + Validation + Data JPA)
- Banco de dados **H2** em memória (recriado e populado a cada execução)
- Maven

## Como executar

```bash
mvn spring-boot:run                                   # perfil padrao: mem
mvn spring-boot:run -Dspring-boot.run.profiles=sql    # implementacao com banco
mvn test                                              # testes automatizados
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
| GET | `/produto?nome=&valorMinimo=&valorMaximo=` | Busca de produtos (filtros opcionais) — via `ProdutoModel` |
| GET | `/produto/{id}` | Visualizar dados do produto (200 ou 404) — via `ProdutoModel` |
| POST | `/produto` | Cadastro de produto (201 + `Location`, ou 400) — via `ProdutoModel` |
| GET | `/cliente/{id}` | Visualizar dados do cliente |
| GET | `/cliente/{idcliente}/formas-de-pagamento` | Listar todas as formas de pagamento (cartões) do cliente |
| GET | `/cliente/{idcliente}/endereco` | Busca pelos dados do endereço do cliente |
| GET | `/tipo-cartao` | Listagem de todos os tipos de cartão |
| GET | `/estado` | Listagem de todos os estados |
| GET | `/estado/{idestado}/cidade` | Listagem de todas as cidades de um estado |
| GET | `/cliente/{idcliente}/pedido` | Listagem de pedidos de um cliente |

Todas as rotas respondem em `application/json`. Erros (400, 404, 405, 415,
500) são devolvidos sempre no mesmo formato JSON pelo `GlobalExceptionHandler`
(`@RestControllerAdvice`).

## Exemplos de teste (com o cliente/produto/categoria/estado de id = 1)

```bash
curl http://localhost:8080/categoria
curl "http://localhost:8080/categoria?nome=eletro"
curl http://localhost:8080/categoria/1/produto
curl http://localhost:8080/produto/1
curl "http://localhost:8080/produto?nome=mouse&valorMinimo=50&valorMaximo=100"
curl -i -X POST http://localhost:8080/produto -H "Content-Type: application/json" \
     -d '{"nome":"Mouse sem fio","descricao":"Mouse optico","valorUnitario":79.90,"categoriaId":1}'
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
├── dto/                              # DTOs de request/response (ProdutoCreateRequest com @Valid)
├── mapper/                           # ProdutoMapper: Request -> Entidade -> Response
├── model/                            # ProdutoModel (interface) + ProdutoModelMemoria / ProdutoModelSql
├── controller/                       # controllers REST (dependem de interfaces, injeção por construtor)
├── exception/                        # @RestControllerAdvice + JSON de erro padronizado
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

## IoC/DI — ProdutoModel

```
ProdutoController ──depende de──> ProdutoModel (interface)
                                       ▲               ▲
                     @Profile("mem")   │               │   @Profile("sql")
                 ProdutoModelMemoria ──┘               └── ProdutoModelSql
                 (lista em memória)                       (Spring Data JPA / H2)
```

- **Contrato:** `ProdutoModel` (`criar`, `obterPorId`, `buscar`). A camada model
  trabalha com a entidade `Produto`; o controller converte
  `ProdutoCreateRequest -> Produto -> ProdutoResponse` com o `ProdutoMapper`.
- **Implementações:**
  - `ProdutoModelMemoria` — `@Service("produtoModelMemoria")`: lista em memória
    com ids gerados por `AtomicLong`. Começa vazia e perde os dados ao reiniciar.
  - `ProdutoModelSql` — `@Service("produtoModelSql")`: usa `ProdutoRepository`
    (JPA) sobre o H2, enxerga os produtos criados pelo `DataSeeder` e valida
    que a categoria informada existe (senão responde 404). Trocando apenas o
    datasource, o mesmo código serve para Postgres/MySQL.
- **Seleção (estratégia adotada: `@Profile`):** `spring.profiles.active=mem` em
  `application.properties`. Para usar a outra implementação, altere para `sql`
  ou rode `mvn spring-boot:run -Dspring-boot.run.profiles=sql` (ou
  `java -jar target/vasscommerce-api-1.0.0.jar --spring.profiles.active=sql`).
  Na inicialização o log informa qual implementação foi injetada:
  `ProdutoController usando a implementacao ProdutoModelMemoria`.
- **Injeção por construtor:** nenhum `@Autowired` em campo no projeto.

### Alternativas (`@Primary` / `@Qualifier`)

Os beans já têm nomes (`produtoModelMemoria` e `produtoModelSql`), então as
outras estratégias do enunciado exigem só trocar anotações:

- `@Primary`: remova os `@Profile` e anote `ProdutoModelMemoria` com `@Primary`.
- `@Qualifier`: remova os `@Profile` e use no construtor do controller
  `public ProdutoController(@Qualifier("produtoModelSql") ProdutoModel model)`.

### Comparando as implementações

| Requisição | perfil `mem` | perfil `sql` |
|---|---|---|
| `GET /produto/1` logo após subir | 404 (memória vazia) | 200 (Smartphone do DataSeeder) |
| primeiro `POST /produto` | id 1 | id 4 (depois dos 3 produtos do DataSeeder) |
| `POST /produto` com `categoriaId` inexistente | 201 (memória não conhece categorias) | 404 `Categoria nao encontrada` |
| `GET /categoria/{id}/produto` | lê sempre do banco (não usa `ProdutoModel`) | lê do banco |

## Postman

A coleção `postman/VassCommerce-IoC-DI.postman_collection.json` tem as
requisições com testes automáticos (201 + Location, 200, 404 e 400). Para
demonstrar a troca de implementação:

1. Suba a API com o perfil `mem`, importe a coleção no Postman, deixe a variável
   de coleção `perfil = mem` e execute **Run collection**.
2. Pare a API, suba com o perfil `sql`, mude `perfil = sql` e execute de novo.

Pela linha de comando, com o [newman](https://www.npmjs.com/package/newman):

```bash
npx newman run postman/VassCommerce-IoC-DI.postman_collection.json --env-var perfil=mem
```

As saídas das duas execuções (17/17 asserções passando em cada perfil) estão em
`docs/evidencias/execucao-perfil-mem.md` e `docs/evidencias/execucao-perfil-sql.md`.

## Correções feitas nesta etapa

Com `spring.jpa.open-in-view=false`, as rotas `/produto/{id}`,
`/categoria/{id}/produto`, `/cliente/{id}/endereco`, `/cliente/{id}/pedido` e
`/estado/{id}/cidade` respondiam **500** (`LazyInitializationException`), porque
os DTOs acessavam relacionamentos `LAZY` depois de fechada a sessão do JPA. Os
repositórios agora carregam esses relacionamentos com `@EntityGraph` /
`join fetch`. Além disso, uma rota inexistente respondia 500 e agora responde
404 no formato de erro padronizado. O `ApiIntegracaoTest` cobre essas rotas.
