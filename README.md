# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução para dispositivos móveis para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java, usando Spring-boot, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

O foco dessa avaliação é a comunicação entre o backend e o aplicativo mobile. Essa comunicação é feita através de mensagens no formato JSON, onde essas mensagens serão interpretadas pelo cliente para montar as telas onde o usuário vai interagir com o sistema. A aplicação cliente não faz parte da avaliação, apenas os componentes do servidor. O formato padrão dessas mensagens será detalhado no anexo 1.

---

### 1. Instrucoes

- versao do Java: 21
- mvn clean install

### 2. Validar que está funcionando

URL API: http://localhost:8080

Documentacao Swagger: http://localhost:8080/swagger-ui.html

### 3. Estrutura do projeto

controller/v1/  # endpoint REST
service         # regras de negocio
repository      # persistencia (Spring Data JPA)
domain          # entidades JPA
dto             # contratos JSON da API
client          # integracao fake de CPF
exception       # tratamento global de erros
config          # configuracoes da aplicacao


### 4. Endpoints (v1)

| Método | URL | Descrição |
|--------|-----|-----------|
| POST | `/api/v1/pautas` | Cadastrar pauta |
| GET | `/api/v1/pautas/{id}` | Consultar pauta |
| POST | `/api/v1/pautas/{pautaId}/sessoes` | Abrir sessão de votação |
| GET | `/api/v1/pautas/{pautaId}/sessoes/atual` | Consultar sessão mais recente |
| POST | `/api/v1/pautas/{pautaId}/votos` | Registrar voto |
| GET | `/api/v1/pautas/{pautaId}/resultado` | Resultado da votação (controller dedicado) |
| GET | `/api/v1/associados/{cpf}/autorizacao` | Validar CPF (bônus) |

### Exemplos de uso

**Cadastrar pauta**

```bash
curl -X POST http://localhost:8080/api/v1/pautas \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Aprovação de orçamento","descricao":"Orçamento anual"}'
```

**Abrir sessão (5 minutos — omitir body usa 1 minuto padrão)**

```bash
curl -X POST http://localhost:8080/api/v1/pautas/1/sessoes \
  -H "Content-Type: application/json" \
  -d '{"duracaoEmMinutos":5}'
```

**Registrar voto**

```bash
curl -X POST http://localhost:8080/api/v1/pautas/1/votos \
  -H "Content-Type: application/json" \
  -d '{"associadoId":"associado-123","voto":"SIM"}'
```

**Consultar resultado**

```bash
curl http://localhost:8080/api/v1/pautas/1/resultado
```

Resposta com maioria:

```json
{
  "pautaId": 1,
  "totalVotos": 3,
  "votosSim": 2,
  "votosNao": 1,
  "resultado": "SIM"
}
```

Resposta em empate (`votosSim` igual a `votosNao`):

```json
{
  "pautaId": 2,
  "totalVotos": 4,
  "votosSim": 2,
  "votosNao": 2,
  "resultado": "EMPATE"
}
```

Valores possíveis de `resultado`: `SIM`, `NAO` ou `EMPATE` (documentado também no Swagger).

### Decisões de arquitetura

- **Camadas**: controller → service → repository, com DTOs na borda da API. Resultado da votação exposto por `ResultadoVotacaoController` dedicado.
- **Persistência**: JPA/Hibernate com H2 em arquivo (default) ou PostgreSQL via profile `postgres`.
- **Regras de negócio**: sessão deve estar aberta para votar; um voto por associado por pauta (constraint única no banco).
- **Contagem de votos**: queries agregadas (`COUNT`) em vez de carregar todos os registros — preparado para alto volume.
- **Logs**: SLF4J nos pontos principais (cadastro, abertura de sessão, registro de voto).
- **Tratamento de erros**: `@RestControllerAdvice` com respostas JSON padronizadas.
