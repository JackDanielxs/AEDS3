# Relatório - TP03

## Participantes
- Daniel Matos Marques

## Descrição

"Presente Fácil" é um sistema que simplifica a organização de eventos por meio do gerenciamento de listas de presentes para qualquer ocasião. Cada usuário tem controle total sobre suas listas, podendo criá-las, consultá-las, editá-las e excluí-las de forma simples e intuitiva.

Também é possível cadastrar produtos nas listas, permitindo adicionar descrição e quantidade, tornando o processo de escolha e compartilhamento dos presentes ainda mais prático e personalizado.

Para compartilhar, o sistema gera um código NanoID, que permite que outras pessoas visualizem as listas sem expor informações sensíveis, garantindo praticidade e segurança.

## O sistema

![Menu de Produtos](./assets/.png)

![Detalhes do Produto](./assets/.png)

![Listagem de Produtos](./assets/.png)

![Detalhes Produto Lista](./assets/.png)

## Classes
- `Usuario`, `ListaPresente` e `Produto` (Classes modelo) - Todas extendem a classe `Registro`, usada como base das entidades.
- `ArvoreBMais` - Implementa o relacionamento **1:N** entre usuários e listas.
- `HashExtensivel` - Implementa índices (diretos e indiretos).
- `NanoID` - Responsável pelos códigos para compartilhar listas entre usuários.

---

- O índice invertido com os termos dos nomes dos produts foi criado usando a classe ListaInvertida?
**R: Sim. A implementação do índice invertido foi feita utilizando a classe ListaInvertida, garantindo uma estrutura eficiente para associar termos aos produtos correspondentes.**
- É possível buscar produtos por palavras no menu de manutenção de produtos?
**R: Sim. O sistema permite realizar buscas por produtos tanto pelo nome quanto pelo GTIN diretamente no menu de manutenção.**
- É possível buscar produtos por palavras na hora de acrescentá-los às listas dos usuários?
**R: Sim. Também é possível buscar produtos por nome ou GTIN ao adicioná-los às listas dos usuários.**
- O trabalho compila corretamente?
**R: Sim.**
- O trabalho está completo e funcionando sem erros de execução?
**R: Sim.**
- O trabalho é original e não a cópia de um trabalho de outro grupo?
**R: Sim.**

---

> Video do funcionamento do TP03 - 
