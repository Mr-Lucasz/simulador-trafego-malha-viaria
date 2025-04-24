Simulador de Tráfego em Malha Viária 🚗🛣️
==========================================

Trabalho 2 -- Sistemas Distribuídos
----------------------------------

**Departamento de Engenharia de Software**\
**Disciplina: Desenvolvimento de Sistemas Paralelos e Distribuídos**\
**Professor: Fernando dos Santos**

### Autores:

-   Lucas da Cunha Rodrigues
-   João Henrique de Carvalho

* * * * *

🧠 Objetivo
-----------

Desenvolver um simulador de tráfego com múltiplos veículos se movimentando por uma malha viária carregada de um arquivo texto. Cada veículo é implementado como uma Thread.

⚙️ Funcionalidades
------------------

-   **Leitura de malha via arquivo texto**
-   **Interface gráfica com visualização da malha e veículos**
-   **Veículos como threads independentes**
-   **Velocidades diferentes para cada veículo**
-   **Escolha aleatória de direção em cruzamentos**
-   **Exclusão mútua com opção de Semáforo ou Monitor**
-   **Controle de:**
    -   Quantidade máxima de veículos simultâneos
    -   Intervalo de inserção de veículos
    -   Início, pausa e encerramento da simulação

🗺️ Malha Viária
----------------

-   **Definida por linhas e colunas**
-   **Cada célula representa um tipo de segmento (estrada ou cruzamento)**
-   **Vias são sempre horizontais ou verticais, com mão dupla**
-   **Entradas e saídas nas bordas**

🛑 Regras de Trânsito
---------------------

-   **Veículo só anda se o próximo espaço estiver livre**
-   **Cruzamentos só podem ser acessados se todos os espaços estiverem disponíveis**
-   **Veículos devem sair da malha ao atingir a borda final**
-   **Não é permitido bloqueio dentro do cruzamento**

* * * * *

Como Executar
-------------

EM BREVE

Requisitos
----------
EM BREV

* * * * *

Contato
-------

Para mais informações ou dúvidas, entre em contato com os autores através dos e-mails:

-   Lucas da Cunha Rodrigues: 

    <l.cunha14.lc@gnail.com>

-   João Henrique de Carvalho: 

    <joao.hc04@gmail.com>

* * * * *

Licença
-------

Este projeto está licenciado sob a Licença MIT - veja o arquivo 

[LICENSE](https://one-uat.bees-platform.dev/LICENSE)

 para mais detalhes.
