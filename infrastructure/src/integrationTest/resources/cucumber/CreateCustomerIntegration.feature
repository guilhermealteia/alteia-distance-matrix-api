#language: pt

Funcionalidade: Cadastro de novo cliente

  Cenario: Criação de um novo cliente em base
    Dado um novo cliente em cadastramento com nome "Guilherme Alteia", aniversario "1984-12-01" e cpf "03250932027"
    Quando o cliente confirmar seus dados
    Entao o cadastro do cliente deve ter sido criado

  Cenario: Criação de um novo cliente em base com nome inválido
    Dado um novo cliente em cadastramento com nome "Guilherme Alteia1", aniversario "1984-12-01" e cpf "03250932027"
    Quando o cliente confirmar seus dados
    Entao o cliente deve receber um erro com o codigo "FLDVLDTNS0001" e a mensagem "O campo name deve atender ao padrão: (?i)^([-'a-zÀ-ÿ ])+$"