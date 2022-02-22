# alteia-gcp-googleapis-udemy

Esqueleto de Microsserviço baseado no clean code e na architecture com Spring Boot

## Regras gerais

* Sempre deixe o código mais limpo do que encontrou
* Utilize nomes descritivos para as variáveis.

Exemplo:

```
//Variável difícil de se contextualizar 
CustomerCreationService service;

//algumas linhas depois...

service.create();

//Variável fácil de se contextualizar 
CustomerCreationService customerCreationService;

//algumas linhas depois...

customerCreationService.create();
```

* Mantenha o padrão de nomenclatura com o qual começou determinada funcionalidade
* Não utilizar imports com (*). Lembre-se de configurar a sua IDE para aceitar o máximo de imports possível. Imports
  específicos são uma forma de sabermos e utilizarmos apenas o que precisamos.

## Domain (Domínio)

Aqui é onde mora o domínio da nossa aplicação e suas regras de negócio.

Módulo: `domain`, caminho: `/src/main/java/br/com/via/microservicechangeit/entities`

* Se a entidade possuir um identificado único, utilize a anotation `@Entity` ao nível de classe e `@Id` ao nível do atributo identificador
* Não esqueça de implementar a interface `Serializable`. Ela é importante para a solução de Cache.
  ```
  public class Customer implements Serializable {
  ```

### Métodos

* Métodos **set** devem ser **privados**
* A entidade deve possuir:
  * Um construtor `protected` vazio
  * Um construtor `public` setando todos os atributos necessários pelos métodos **set**

```
public Counter(String id, String name, Integer value) {
  setId(id);
  setName(name);
  setValue(value);
}
```
* Defina validadores para os métodos de entrada de dados (sets) conforme as regras de sua entidade.
* Os validadores podem retornar exceções para cada regra violada (como está no exemplo) ou pode-se estabelecer uma lista de exceções com todas as regras violadas de uma só vez (no nosso caso, não faz muito sentido e não está no exemplo)
```
private void setName(String name) {
  standardizedSizedString(name, ...);
  this.name = name;
}
```
* Sobrescreva os métodos `equals`, `hashCode` e `toString`. As entidades devem ser comparáveis ao nível de atributos e
  não de memória.
* Há atalhos de teclado para gerar esses métodos automaticamente
* Não inclua o id da entidade nos métodos `equals` e `hashCode`, apenas no `toString`
  ```
  @Override
  public boolean equals(Object o) {
  if (this == o) return true;
  if (o == null || getClass() != o.getClass()) return false;
     Customer customer = (Customer) o;
     return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(birthday, customer.birthday);
  }
  
  @Override
  public int hashCode() {
     return Objects.hash(id, name, birthday);
  }
  
  @Override
  public String toString() {
  
  return "{" +
     "id=" + id +
     ", name='" + name + '\'' +
     ", birthday=" + birthday + '}';
  }
  ```

## Data Transfer Objects (DTOs)
Não podemos cruzar fronteiras com as nossas entidades. Portanto, aqui moram os objetos que transferem dados entre a camada de use_cases e a controller.

Módulo: `use_cases`, caminho: `/src/main/java/br/com/via/microservicechangeit/use_cases/(nome do domínio)/dto`

### Métodos

* Defina os gets e sets para todos os atributos
* Sobrescreva apenas os métodos `equals` e `hashCode` para tornar os DTOs comparáveis ao nível de atributos e não de
  memória.
  ```
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CreateCustomerRequestDto that = (CreateCustomerRequestDto) o;
      return Objects.equals(name, that.name) && Objects.equals(birthday, that.birthday);
  }

  @Override
  public int hashCode() {
      return Objects.hash(name, birthday);
  }
  ```
### Estrutura
* Apesar de o swagger ser um ferramental externo ao pacote java, ele pode ser utilizado nos DTOs a fim de documentar o sistema.
* Ele não interfere no funcionamento da aplicação.
* Caso não tenha nenhuma anotação de swagger no DTO, ele ainda será mapeado, porém sem as descrições que facilitam seu entendimento.
* No campo `birthday` da classe abaixo, note a annotation `@ApiModelProperty`
  * Em `value`, define-se a descrição do campo
  * Em `example`, define-se um valor de exemplo para o campo `"2021-12-31"`
  * Em `required`, indica-se se o campo é obrigtório ou não

```
public class CreateCustomerRequestDto {

    @ApiModelProperty(value = "Nome do cliente", required = true)
    private String name;

    @ApiModelProperty(value = "Data de nascimento do cliente", example = SWAGGER_DATE_ISO8601, required = true)
    private String birthday;

    @ApiModelProperty(value = "CPF do cliente", example = SWAGGER_CPF, required = true)
    private String cpf;

    public CreateCustomerRequestDto() {
    }

    public CreateCustomerRequestDto(String name, String birthday, String cpf) {
        this.name = name;
        this.birthday = birthday;
        this.cpf = cpf;
    }

    public CreateCustomerRequestDto(Customer customer) {
        if (Objects.nonNull(customer)) {
            this.name = customer.getName();
            this.birthday = customer.getBirthday().toString();
            this.cpf = customer.getCpf();
        }
    }

    public Customer toCustomer() {
        return new Customer(null, getName(), fromStringToLocalDateISO601(getBirthday()), getCpf());
    }
```

## Use Cases (Casos de uso)
Aqui é onde moram as automatizações que criamos da necessidade de negócio que temos.

* Para usar bancos de dados, mensageria, ..., devemos usar interfaces que serão posteriormente implementadas na camada de infraestrutura.

Módulo: `use_cases`, caminho: `/src/main/java/br/com/via/microservicechangeit/use_cases/(nome do domínio)`

## Controllers (Adaptadores de interface)
Aqui é onde moram as conversões que fazemos entre as camadas que acessam a nossa aplicação e os use cases.
* Geralmente, as camadas externas da aplicação não entregam exatamente o que um `use_case` espera receber para o processamento.
* Aqui podem ser utilizadas interfaces de conversão

Módulo: `controller`, caminho: `/src/main/java/br/com/via/microservicechangeit/(nome do domínio)`

## Overview

Aplicação utilizando a Clean
Architecture: (https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

Temos 3 pacotes essenciais:

* `domain`: Contém as regras e lógica de negócio sem dependência de frameworks
* `use_cases`: Dependem apenas do pacote `domain` para executar as regras de negócio. Não podem depender do
  pacote `infrastructure`
* `infrastructure`: Contém detalhes técnicos, configurações, camadas de base de dados, controllers, ...e não podem
  conter regras de negócio. Podem possuir dependências com frameworks.

# Subindo a aplicação fora do container (Pela IDE,...)

## 1 - Suba as dependências da aplicação com o docker-compose para os testes locais

```
docker-compose -f docker-compose-only-redis.yaml up -d
```

## 2 - Rode a aplicação localmente

```
gradle bootRun
```
ou
```
./gradlew bootRun
```

# Rode a aplicação no container docker

```
docker-compose up -d --build  
```

1 - Para acessar os logs via kibana vá a URL http://localhost:5601/app/management/kibana/indexPatterns
e crie um index pattern com o texto fluentd-*. Salve e depois vá na seção "Discover", os logs da aplicação
estarão listados ali

2 - Para parar a aplicação e suas dependências, execute o comando:
```
docker-compose down -v  
```

## Testes

* Integration tests
```
gradle :infrastructure:intregrationTest
```

* Unit tests
```
gradle :infrastructure:unitTest
```

* Bdd tests
```
gradle :infrastructure:bddTest
```

## Sonar issues e cobertura de testes (coverage)
Rode o comando abaixo com os containeres locais ativos para o passo do Sonar:
```
gradle codeCoverageReport sonarqube --info
```

## Utilidades:

### Anexe-se ao container redis

```
docker exec -it redis bash
```

### Execute uma verificação de latência:

```
redis-cli --latency -h localhost -p 6379
```

### Monitore as chamadas entrantes

```
redis-cli monitor
```

### Gere e execute uma imagem docker:

```
docker build  --tag=alteia/alteia-gcp-googleapis-udemy:latest .
docker run -p 8080:8080 -ti --name cleanarch-relational alteia/alteia-gcp-googleapis-udemy
```

## Install

```
./gradlew assemble
```

## Testes

```
./gradlew check
```