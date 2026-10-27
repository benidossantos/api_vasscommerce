# Execucao da colecao Postman (newman) com spring.profiles.active=mem

## Log de inicializacao da API
ProdutoController usando a implementacao ProdutoModelMemoria

## Exemplos de respostas (curl)

$ curl -i http://localhost:8080/produto/1
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked

{"timestamp":"2026-10-27T00:32:55.67699739","status":404,"erro":"Not Found","mensagem":"Produto nao encontrado com id 1","caminho":"/produto/1"}


$ curl -i -X POST http://localhost:8080/produto -H "Content-Type: application/json" -d '{"nome":"Mouse sem fio","descricao":"Mouse optico","valorUnitario":79.90,"categoriaId":1}'
HTTP/1.1 201 
Location: http://localhost:8080/produto/1
Content-Type: application/json
Transfer-Encoding: chunked

{"id":1,"nome":"Mouse sem fio","descricao":"Mouse optico","fotoUrl":null,"valorUnitario":79.90,"categoriaId":1,"categoriaNome":null,"dataCadastro":"2026-10-27T00:32:55.815992617","dataUltimaAtualizacao":"2026-10-27T00:32:55.815992617"}


$ curl -i -X POST http://localhost:8080/produto -H "Content-Type: application/json" -d '{"nome":"","valorUnitario":-10}'
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Connection: close

{"timestamp":"2026-10-27T00:32:55.848458785","status":400,"erro":"Bad Request","mensagem":"Dados invalidos na requisicao","caminho":"/produto","campos":[{"campo":"categoriaId","mensagem":"categoriaId e obrigatorio"},{"campo":"descricao","mensagem":"descricao e obrigatoria"},{"campo":"nome","mensagem":"nome e obrigatorio"},{"campo":"valorUnitario","mensagem":"valorUnitario deve ser maior que zero"}]}


$ curl -i http://localhost:8080/produto/999999
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked

{"timestamp":"2026-10-27T00:32:55.859944852","status":404,"erro":"Not Found","mensagem":"Produto nao encontrado com id 999999","caminho":"/produto/999999"}


## Newman

```
newman

VassCommerce - IoC/DI (ProdutoModel)

Root 1. GET /produto/1 (antes de criar) - mostra a troca de implementacao
  GET http://localhost:8080/produto/1 [404 Not Found, 316B, 52ms]
  Pass  [mem] 404 - a lista em memoria comeca vazia
  Pass  [mem] erro JSON padronizado

Root 2. POST /produto (valido) -> 201 + Location
  POST http://localhost:8080/produto [201 Created, 511B, 190ms]
  Pass  201 Created
  Pass  Header Location aponta para o novo produto
  Pass  Corpo com os dados do produto
  -
  | 'Perfil mem gerou id 1'
  -

Root 3. GET /produto/{id} (criado) -> 200
  GET http://localhost:8080/produto/1 [200 OK, 463B, 8ms]
  Pass  200 OK
  Pass  Retorna o produto criado

Root 4. GET /produto/999999 -> 404
  GET http://localhost:8080/produto/999999 [404 Not Found, 326B, 9ms]
  Pass  404 Not Found
  Pass  Erro JSON padronizado

Root 5. POST /produto (invalido) -> 400
  POST http://localhost:8080/produto [400 Bad Request, 546B, 28ms]
  Pass  400 Bad Request
  Pass  Erro JSON padronizado com a lista de campos invalidos

Root 6. POST /produto (JSON malformado) -> 400
  POST http://localhost:8080/produto [400 Bad Request, 304B, 11ms]
  Pass  400 Bad Request
  Pass  Mensagem padronizada

Root 7. GET /produto?nome=mouse&valorMinimo=50&valorMaximo=100 -> 200
  GET http://localhost:8080/produto?nome=mouse&valorMinimo=50&valorMaximo=100 [200 OK, 465B, 46ms]
  Pass  200 OK
  Pass  Filtra pelo nome e pela faixa de valor

Root 8. GET /produto (todos) -> 200
  GET http://localhost:8080/produto [200 OK, 465B, 8ms]
  Pass  200 OK
  Pass  [mem] quantidade de produtos depende da implementacao

------------------------------------------------------------------
|                         |          executed |           failed |
--------------------------+-------------------+-------------------
|              iterations |                 1 |                0 |
--------------------------+-------------------+-------------------
|                requests |                 8 |                0 |
--------------------------+-------------------+-------------------
|            test-scripts |                 8 |                0 |
--------------------------+-------------------+-------------------
|      prerequest-scripts |                 0 |                0 |
--------------------------+-------------------+-------------------
|              assertions |                17 |                0 |
------------------------------------------------------------------
| total run duration: 542ms                                      |
------------------------------------------------------------------
| total data received: 2.06kB (approx)                           |
------------------------------------------------------------------
| average response time: 44ms [min: 8ms, max: 190ms, s.d.: 57ms] |
------------------------------------------------------------------
```
