# WePayU (Relatório) - Projeto de Programação 2 (P2)

### Discente: Sallys Carlos da Silva
<hr>

<h2>Informações gerais</h2>
<p>Neste relatório, constam informações importantes sobre os padrões utilizados no desenvolvimento da aplicação, além de exemplificações.</p>
<p>Foram utilizados no projeto os seguintes padrões de projeto: <b>Facade</b>, <b>Camadas</b>, <b>Services</b>, <b>Models</b> e <b>DAO</b>.</p>

<h2>Facade</h2>
<p>O padrão de projeto "Facade" permite a simplificação da estrutura de uma aplicação, fornecendo uma interface unificada para um conjunto de interfaces em um subsistema. Ele foi desenvolvido com o intuito de ocultar a complexidade existentes nas classes internas do sistema, funcionando como uma camada de abstração.</p>
<h4>Exemplo de uso:</h4>
<img src="images/print1.png">

<h2>Camadas</h2>
<p>Esse padrão consiste na separação das funcionalidades do sistema em camadas distintas, com responsabilidades únicas e definidas. Esse padrão permite uma modularização do sistema e o encapsulamento dos dados, além de facilitar a organização das regras de negócio.</p>

<h2>Models</h2>
<p>Os "models" são responsáveis por organizar e estruturar os modelos de dados da aplicação. Os "models" representam as entidades do sistema e frequentemente incluem lógica relacionada à manipulação e validação dos dados.</p>
<h4>Exemplo de uso:</h4>
<img src="images/print3.png">

<h2>Service</h2>
<p> Os "services" são módulos (classes) que contém a lógica de negócios da aplicação. Eles são responsáveis por coordenar as operações entre os "models", executando lógica de negócios e orquestrando a interação entre diferentes partes da aplicação. Está intrinsecamente relacionado ao padrão em camadas.</p>
<h4>Exemplo de uso:</h4>
<img src="images/print2.png">

<h2>DAO</h2>
<p>O Data Access Object (DAO) é responsável por separar a lógica de negócio do acesso aos dados da aplicação. Esse padrão permite um melhor encapsulamento do acesso aos dados, além de facilitar aspectos como testabilidade, flexibilização e reutilização de código.</p>
<h4>Exemplo de uso:</h4>
<img src="images/print.png">