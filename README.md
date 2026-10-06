# Frota API

API REST (Spring Boot 3 + H2) para gerenciar a frota de veiculos de uma empresa.

## Estrutura
```
src/main/java/com/frota/api
├── FrotaApplication.java
├── model/Veiculo.java
├── repository/VeiculoRepository.java
├── service/VeiculoService.java
└── controller/VeiculoController.java
```

## Como rodar
```bash
mvn spring-boot:run     # sobe em http://localhost:8080
mvn test                # roda os testes automatizados
```
Console do H2: http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:file:./dbProdutos`, usuario `root`, senha `root`)

## Endpoints
| Metodo | Rota             | Descricao             |
|--------|------------------|-----------------------|
| POST   | /veiculos        | Cria um veiculo       |
| GET    | /veiculos        | Lista todos           |
| GET    | /veiculos/{id}   | Busca por id          |
| PUT    | /veiculos/{id}   | Atualiza              |
| DELETE | /veiculos/{id}   | Remove                |

Exemplo:
```bash
curl -X POST localhost:8080/veiculos -H "Content-Type: application/json" \
  -d '{"placa":"ABC-1234","marca":"Fiat","modelo":"Uno","ano":2015}'
```
