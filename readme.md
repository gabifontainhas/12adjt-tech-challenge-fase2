# Tech Challenge Fase 2 - Sistema de Gerenciamento de Restaurantes

Turma ADJ12

## 📋 APIs Desenvolvidas

### 👥 Gestão de Usuários e Perfis

A API permite gerenciar perfis de usuários distintos utilizando estratégias de herança de entidades no domínio:
* **Customer (Cliente):** Cadastro e gerenciamento de informações dos clientes.
* **Owner (Dono de Restaurante):** Cadastro dos proprietários dos estabelecimentos.
* **Funcionalidades:** Criação de novos usuários, alteração de dados cadastrais, consulta de informações e exclusão de cadastros de acordo com o perfil selecionado.

### 🏢 Gestão de Restaurantes e Cardápios
* **Restaurant (Restaurante):** API responsável pelo cadastro de restaurantes, incluindo validações de endereço, horário de funcionamento, tipo de cozinha e atribuição ao seu respectivo proprietário (Owner).
* **MenuItem (Itens do Cardápio):** Gerenciamento do cardápio de cada restaurante, permitindo a criação, alteração, consulta e exclusão de pratos, com atributos de preço, descrição, flag de consumo local (*dine-in only*) e caminho da imagem.

## 🏗️ Arquitetura do Projeto

Para cumprir os requisitos de escalabilidade e manutenibilidade da fase, o projeto foi estruturado seguindo rigorosamente os conceitos de **Clean Architecture** e **SOLID**:
* **Domain:** Contém as entidades puras de negócio (`User`, `Customer`, `Owner`, `Restaurant`, `MenuItem`) e os Objetos de Valor (`Address`). Esta camada é totalmente isolada e não possui dependências de frameworks.
* **Application:** Contém as regras de negócio da aplicação através dos Casos de Uso (`Use Cases`) e declara as interfaces (gateways) de comunicação externa.
* **Infrastructure:** A camada mais externa, responsável por frameworks, drivers e integrações:
  * **`infrastructure.web`:** Contém os **Controllers** (adaptadores de entrada), responsáveis por expor os endpoints da API, receber as requisições HTTP, validar os DTOs e delegar a execução para os Use Cases correspondentes.
  * **`infrastructure.gateway`:** Contém os **Adapters** que implementam as interfaces (gateways) definidas na camada de *Application*. Eles realizam a ponte entre as regras de aplicação e a persistência, fazendo a conversão/mapeamento de dados.
  * **`infrastructure.persistence`:** Contém os detalhes específicos da persistência JPA — as entidades do banco de dados (mapeadas com as anotações do Hibernate) e as interfaces que herdam de `JpaRepository` (Spring Data JPA)
* **`config/` (Raiz):** Classes de configuração do Spring Boot. Como as camadas de `domain` e `application` são escritas em Java puro (livres de acoplamento e sem anotações do Spring como `@Service` ou `@Component`), este pacote é responsável por instanciar manualmente os Casos de Uso como `@Bean`s do Spring, viabilizando a injeção de dependências sem poluir o núcleo do sistema.

## 🛠 Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.1.0
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker & Docker Compose
* **Testes de Unidade:** JUnit, Mockito e Jacoco (garantindo cobertura superior a 80%)
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

## 🧪 Cobertura de Testes e Relatório JaCoCo
Para rodar a suite de testes e gerar o relatório detalhado de cobertura de código (que ultrapassa a meta de 80% exigida), execute:

`mvn clean verify`

O relatório em formato interativo HTML será gerado na pasta:

[target/site/jacoco/index.html]()

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

