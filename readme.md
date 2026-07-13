# Tech Challenge - Sistema de Gerenciamento de Restaurantes

Fase 2 do Tech Challenge - Turma ADJ12

APIs responsáveis pela gestão de usuários do sistema de gerenciamento de restaurantes, tendo dois perfis principais:
* Customer (Cliente)
* Owner (Dono de Restaurante)
* É possível criar novos usuários, alterar seus dados, exclusão de cadastros e consulta de usuários, de acordo com o perfil selecionado.


APIs responsáveis pela gestão de restaurantes e itens do cardápio
* Restaurant (Restaurant)
* MenuItem (itens do cardápio)
* É possível criar novos restaurantes, alterar seus dados, exclusão e consulta, além de permitir a criação de novos itens de cardápio associados ao restaurante, bem com a manipulação de suas informações.


## 🛠 Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.0.5
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker & Docker Compose
* **Testes de Unidade:** JUnit, Mockito e Jacoco (para visualizar cobertura)
* **Testes de Integração:** RestAssured
* **Testes manuais dos endpoints:** Postman

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* [Docker](https://www.docker.com/get-started) instalado.
* [Git](https://git-scm.com/) instalado.

### Passo a passo

1. Clone o repositório:
   _git clone_ https://github.com/gabifontainhas/12adjt-tech-challenge-fase2.git
2. Executar _mvn clean install_ para gerar o artefato .jar na target
3. Executar o comando _docker build -t tech-challenge2:latest ._
4. Executar o comando _docker compose up_
5. A aplicação estará disponível em http://localhost:8080

## 🧪 Coleção do Postman para Testes Manuais
Para validação dos fluxos, foi disponibilizada uma coleção para o Postman, com cenários de sucesso e erro:

🔗 [Postman](https://github.com/gabifontainhas/12adjt-tech-challenge-fase2/blob/b46dd61157e87d9586d813476f427f16329ab7d1/postman/TechChallenge_Fase2.postman_collection.json
)

Para importar a coleção:
1. Abra o Postman.
2. Clique em File > Import.
3. Arraste ou selecione o arquivo baixado.

## ✒️ Autora

Gabriela Fontainhas - [LinkedIn](https://www.linkedin.com/in/gabriela-fontainhas-5a8b0935/) - [GitHub](https://github.com/gabifontainhas/)

