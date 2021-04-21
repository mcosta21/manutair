## ManutAir

<details>
<summary><strong>Minimundo</strong></summary>

A empresa ManutAir Ltda. tem como atividade a prestação de serviços de manutenção de equipamentos de condicionamento de ar. O quadro de funcionários da empresa é composto de técnicos em ar-condicionado e pessoal administrativo.

A empresa possui contratos com diversos Clientes. Quando um Cliente fecha um novo contrato, ele deve informar ao Atendente a razão social, endereço, CNPJ, nome e telefone do responsável, para o caso de Cliente PJ, ou nome, endereço, telefone e CPF, para o caso de Cliente PF. O sistema busca e exibe os dados do Cliente (tipicamente os dados já́ se encontram cadastrados no sistema). Caso os dados do Cliente ainda não se encontrem cadastrados, esse é o momento de fazê-lo. Em ambos os casos o Cliente informa, também, a lista dos equipamentos cobertos pelo contrato (através de sua marca, modelo e número de série), a data de início da vigência e o prazo de duração em meses.

Os contratos, após cadastrados, recebem do sistema um número. A lista de equipamentos cobertos pode ser alterada com a inclusão ou exclusão de novos equipamentos.

Os Clientes, quando necessitam de algum atendimento, ligam para o número telefônico de solicitação de serviço. As chamadas são recebidas pelos Atendentes que fazem a abertura das Ordens de Serviço (OS), deixando-as com status “Aberta”. Para tal, os Atendentes solicitam ao Cliente o número do contrato (que tipicamente já́ está cadastrado no sistema), o equipamento que necessita reparo (marca, modelo, número de série), o endereço onde este se encontra e uma breve descrição do problema.

O Supervisor Técnico disporá́ de uma funcionalidade para consulta e alocação de novas OS abertas aos técnicos de campo. Ao fazer a alocação de uma OS a um técnico o Supervisor anota o dia, a hora marcada para a visita do Técnico, deixando a OS com status “Em Andamento”.

Os Técnicos realizam as visitas aos Clientes, onde prestam o atendimento solicitado e encerra a OS, atribuindo o status “Concluída”. Ele só deve finalizar a OS quando realmente concluir o serviço.

A empresa recebe de 50 a 70 chamados por dia e trabalha com um Supervisor, 2 Atendentes,15 Técnicos de campo.
</details>

### Diagramas UML

#### Diagrama de classes
![Diagrama de classes](https://github.com/mcosta21/manutair/blob/main/docs/Diagrama%20de%20classes.png)

#### Diagrama de casos de uso
![Diagrama de casos de uso](https://github.com/mcosta21/manutair/blob/main/docs/Diagrama%20de%20caso%20de%20uso.png)

#### Diagrama de máquina de estados da Ordem de Serviço
![Diagrama de máquina de estados da Ordem de Serviço](https://github.com/mcosta21/manutair/blob/main/docs/Diagrama%20de%20m%C3%A1quina%20de%20estados%20da%20ordem%20de%20servi%C3%A7o.png)

### Estrutura de arquivos

- database
- docs
- controller
- domain
  - dao
  - enumeration
  - model
  - validator
  - util
- resources
  - fonts
  - fxml
  - styles
- target

### Controle de acesso

Para estabelecer o controle de acesso as funções por tipo de usuário (UserTypeEnum), foi criado a classe abstrata AccessProvider. De modo que a partir da consulta do tipo de usuário logado, é retornado uma lista de páginas definida pela classe Page (Composta por nome do arquivo e título). Assim, a classe MainController é capaz de montar com a função generateOptionPages, uma lista de botões para cada página a fim de liberar o acesso a mesma.

### Validadores

Dado a necessidade de válidar as informações inseridas para cada entidade do sistema, foi utilizado o conceito de valitors, onde para cada umas das classes listadas abaixo, criou-se funções booleanas para verificar cada input, ao qual visa permitir ou não que o usuário finalize o processo.

- Client
- Contract
- Equipment
- ServiceOrder
- User

### Util

Devido a vasta reuzabilidade de um recurso por grande parte do sistema, diversas classes foram criadas e unificadas no pacote util, e assim fornecer sua usabilidade de forma generica, como é o caso do AccessProvider para fornecer o usuário logado em todas as páginas como uma se fosse uma sessão do navegador, ou ManagerWindow, responsável por abrir e fechar uma tela apenas pelo nome do arquivo fxml e evento de clique do botão, respectivamente.

> Foi utilizado a lib **Lombok** para geração automática dos métodos como getter e setter.

### Usuários de teste

*Senha padrão: **123** *

1. admin (Administrador)
2. alan (Supervisor)
3. marcio (Atendente)
3. marcio (Atendente)
4. joao (Técnico)
5. adriel (Técnico)
6. thiago (Técnico)