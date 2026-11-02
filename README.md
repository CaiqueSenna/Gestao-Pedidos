# 📦 Gestão de Pedidos

Aplicativo Android desenvolvido em **Kotlin** para gerenciar **produtos, clientes e pedidos**, com painel de indicadores e geração de pedidos em PDF.

O projeto organiza suas responsabilidades com **MVVM**, princípios de **Clean Architecture**, injeção de dependências com **Hilt**, persistência local com **Room** e interface em **Jetpack Compose**.

## ✨ Funcionalidades

- **Dashboard:** visão geral com quantidade de produtos, clientes, pedidos e valor acumulado de pedidos.
- **Produtos:** telas de listagem e formulário, com operações de cadastro, consulta, atualização e exclusão estruturadas no projeto. Os dados incluem nome, descrição, preço e estoque.
- **Clientes:** listagem e formulário para cadastro e manutenção de nome, e-mail, telefone e documento.
- **Pedidos:** criação de pedidos associados a clientes, com itens, quantidades, preços unitários, observações e total calculado.
- **PDF:** geração local de documento do pedido e estrutura para compartilhamento.
- **Persistência:** armazenamento local com Room e consultas reativas com Kotlin Flow.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Aplicação |
| --- | --- |
| Kotlin | Linguagem principal |
| Jetpack Compose + Material 3 | Interface de usuário |
| MVVM | Separação entre apresentação e estado |
| Clean Architecture | Organização por camadas e casos de uso |
| Hilt | Injeção de dependências |
| Room | Banco de dados SQLite local |
| Kotlin Coroutines e Flow | Operações assíncronas e observação de dados |
| Navigation Compose | Navegação entre telas |
| Android `PdfDocument` | Geração de PDF |
| Gradle Kotlin DSL + Version Catalog | Configuração de build e dependências |

## 🏗️ Arquitetura

```text
GestaoPedidos/
└── app/src/main/java/com/caiquesenna/gestaopedidos/
    ├── data/
    │   ├── local/
    │   │   ├── dao/              # ClienteDao, PedidoDao, ProdutoDao
    │   │   ├── entity/           # Entidades do Room
    │   │   └── AppDatabase.kt
    │   ├── mapper/               # Conversão entre entidades e modelos
    │   ├── pdf/                  # Implementação do gerador de PDF
    │   └── repository/           # Implementações dos repositórios
    ├── di/                       # Módulos do Hilt
    ├── domain/
    │   ├── model/                # Cliente, Produto, Pedido
    │   ├── pdf/                  # Contrato do gerador de PDF
    │   ├── repository/           # Contratos dos repositórios
    │   └── usecase/              # Regras de negócio
    │       ├── cliente/
    │       ├── dashboard/
    │       ├── pedido/
    │       └── produto/
    ├── ui/
    │   ├── cliente/              # Telas e ViewModels
    │   ├── dashboard/
    │   ├── navigation/
    │   ├── pedido/
    │   ├── produto/
    │   ├── theme/
    │   └── util/
    ├── GestaoApp.kt
    └── MainActivity.kt
```

### Fluxo dos dados



**Presentation (`ui`):** exibe informações, recebe ações e observa o estado dos ViewModels.

**Domain (`domain`):** define modelos, contratos e regras de negócio. Por exemplo, um pedido precisa ter cliente, ao menos um item e quantidades positivas.

**Data (`data`):** implementa os repositórios e integra a camada de domínio ao banco de dados Room.

**DI (`di`):** disponibiliza banco de dados, DAOs e repositórios por meio do Hilt.

## 📱 Telas do aplicativo

| Tela | Objetivo |
| --- | --- |
| Dashboard | Indicadores de produtos, clientes, pedidos e valores |
| Lista de produtos | Consultar e gerenciar produtos |
| Formulário de produto | Cadastrar ou editar informações |
| Lista de clientes | Consultar e gerenciar clientes |
| Formulário de cliente | Cadastrar ou editar informações |
| Lista de pedidos | Consultar pedidos cadastrados |
| Formulário de pedido | Selecionar cliente e montar pedido |

## Capturas de tela









## 👨‍💻 Sobre o projeto

Projeto voltado ao estudo e à prática de desenvolvimento Android com Kotlin, persistência local, separação de responsabilidades e organização escalável do código.

