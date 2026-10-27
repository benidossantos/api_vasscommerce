# CHECKLIST — Separação Controller x Model (IoC/DI)

- (x) **Controladores não fazem `new` de implementações; dependem de interfaces.**
  `ProdutoController` recebe apenas `ProdutoModel` (interface) no construtor e
  não importa `ProdutoModelMemoria` nem `ProdutoModelSql`
  (`src/main/java/com/vasscommerce/api/controller/ProdutoController.java`).
  Nenhum controller do projeto instancia serviços/models com `new`.

- (x) **Há ao menos duas implementações `@Service` de `ProdutoModel`.**
  - `ProdutoModelMemoria` — `@Service("produtoModelMemoria")`: lista em memória + `AtomicLong`.
  - `ProdutoModelSql` — `@Service("produtoModelSql")`: Spring Data JPA sobre o banco H2.

  Ambas em `src/main/java/com/vasscommerce/api/model/`.

- (x) **A seleção é feita por `@Primary` ou `@Qualifier` ou `@Profile`.**
  Estratégia escolhida: **`@Profile`**. `ProdutoModelMemoria` tem `@Profile("mem")`,
  `ProdutoModelSql` tem `@Profile("sql")` e o perfil ativo fica em
  `application.properties` (`spring.profiles.active=mem`). Para trocar:
  `mvn spring-boot:run -Dspring-boot.run.profiles=sql`.
  Prova:
  - testes `SelecaoPorProfileMemTest` e `SelecaoPorProfileSqlTest` verificam qual
    classe foi injetada e que o outro bean nem chega a ser criado;
  - na inicialização, o log mostra
    `ProdutoController usando a implementacao ProdutoModelMemoria` (ou `ProdutoModelSql`);
  - `docs/evidencias/execucao-perfil-mem.md` e `docs/evidencias/execucao-perfil-sql.md`:
    a mesma coleção Postman dá `GET /produto/1` → 404 no perfil `mem` (memória vazia)
    e 200 no perfil `sql` (produto do DataSeeder).

- (x) **Injeção é por construtor; nenhum `@Autowired` em campo.**
  Todos os controllers, models e o `DataSeeder` recebem as dependências pelo
  construtor, em campos `final`. Não há `@Autowired` em `src/main`
  (`grep -r "@Autowired" src/main` não retorna nada).

- (x) **`ProdutoController` continua retornando 201/200/404 corretos.**
  - `POST /produto` → **201 Created** + header `Location: http://localhost:8080/produto/{id}`;
  - `GET /produto/{id}` → **200 OK** ou **404 Not Found** (JSON padronizado);
  - `GET /produto?nome=&valorMinimo=&valorMaximo=` → **200 OK**.
  - `produces = "application/json"` na classe e `consumes = "application/json"` no POST
    (Content-Type diferente → 415).

  Coberto por `ProdutoControllerTest` e pela coleção Postman.

- (x) **Validação em `ProdutoCreateRequest` continua ativa (`@Valid`).**
  `@NotBlank` em `nome`/`descricao`, `@NotNull` + `@DecimalMin("0.01")` + `@Digits`
  em `valorUnitario`, `@NotNull` + `@Positive` em `categoriaId`, `@Size` nos textos.
  O controller usa `@Valid @RequestBody ProdutoCreateRequest`.

- (x) **JSON de erro 400 continua padronizado via `@RestControllerAdvice`.**
  `GlobalExceptionHandler` devolve sempre o mesmo formato
  (`timestamp`, `status`, `erro`, `mensagem`, `caminho` e, em erros de validação,
  `campos[]`), para validação (400), JSON malformado (400), parâmetro inválido (400),
  recurso inexistente (404), rota inexistente (404), método não suportado (405),
  Content-Type não suportado (415) e erros inesperados (500).

Exemplo de 400:

```json
{
  "timestamp": "2026-10-27T00:33:12.062043007",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados invalidos na requisicao",
  "caminho": "/produto",
  "campos": [
    { "campo": "categoriaId", "mensagem": "categoriaId e obrigatorio" },
    { "campo": "descricao", "mensagem": "descricao e obrigatoria" },
    { "campo": "nome", "mensagem": "nome e obrigatorio" },
    { "campo": "valorUnitario", "mensagem": "valorUnitario deve ser maior que zero" }
  ]
}
```

## Testes automatizados (`mvn test`): 18 testes, todos passando

| Classe | O que prova |
|---|---|
| `ProdutoModelMemoriaTest` | teste unitário puro da implementação em memória (criar, obterPorId, buscar com filtros) |
| `ProdutoControllerTest` | `@WebMvcTest` com `ProdutoModel` mockado: 201 + Location, 200, 404, 400 (validação e JSON malformado), 415 |
| `SelecaoPorProfileMemTest` | com o perfil `mem`, o bean injetado é `ProdutoModelMemoria` |
| `SelecaoPorProfileSqlTest` | com o perfil `sql`, o bean injetado é `ProdutoModelSql` e ele lê/grava no banco |
| `ApiIntegracaoTest` | as rotas GET das etapas anteriores continuam respondendo 200/404 |
