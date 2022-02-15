#language: pt

Funcionalidade: Cadastro de novo cliente

  Cenario: Criação de um novo cliente em base
    Dado um novo cliente em cadastramento
    Quando o cliente inserir seu nome "Guilherme Alteia", aniversario "1984-12-01" e cpf "03250932027"
    Entao o cadastro do cliente deve constar o nome "Guilherme Alteia", o aniversario "1984-12-01" e o cpf "03250932027"

  Cenario: Criação de um novo cliente em base com nome inválido
    Dado um novo cliente em cadastramento
    Quando o cliente inserir seu nome "Guilherme Alteia1", aniversario "1984-12-01" e cpf "03250932027"
    Entao o cliente deve receber um erro com a mensagem "O campo name deve atender ao padrão: ^[A-Za-z ]*$"