# cleanarc-springboot

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

## Regras sobre entidades

Módulo: `domain`, pacote: `/src/main/java/br/com/alteia/microservicechangeit/entities`

* Se a entidade possuir um identificado único, utilize a anotation `@Entity` ao nível de classe e `@Id` ao nível do atributo identificador 
* Não esqueça de implementar a interface `Serializable`. Ela é importante para a solução de Cache.
  ```
  public class Customer implements Serializable {
  ```

### Métodos

* Métodos set devem ser privados
* A entidade deve possuir um construtor `protected` vazio e um construtor `public` com todos os atributos necessários, sendo preenchidos pelos métodos set

```
public Counter(String id, String name, Integer value) {
  setId(id);
  setName(name);
  setValue(value);
}
```
* Defina os validadores para os métodos de entrada de dados (sets)
```
private void setName(String name) {
  standardizedSizedString(name, ...);
  this.name = name;
}
```
* Sobrescreva os métodos `equals`, `hashCode` e `toString`. As entidades devem ser comparáveis ao nível de atributos e
  não de memória.

OBS: Não adicione o atributo identificador de uma entidade  no equals() e hashCode().
  ```
  @Override
  public boolean equals(Object o) {
  if (this == o) return true;
  if (o == null || getClass() != o.getClass()) return false;
     Customer customer = (Customer) o;
     return Objects.equals(name, customer.name) && Objects.equals(birthday, customer.birthday);
  }
  
  @Override
  public int hashCode() {
     return Objects.hash(name, birthday);
  }
  
  @Override
  public String toString() {
  
  return "{" +
     "id=" + id +
     ", name='" + name + '\'' +
     ", birthday=" + birthday + '}';
  }
  ```

## Regras sobre DTOs

Módulo: `controller`, pacote: `/src/main/java/br/com/alteia/microservicechangeit/*/dto`

### Métodos

* Denina os gets e sets para todos os atributos
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

## Overview

Aplicação utilizando a Clean
Architecture: (https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

Temos 3 pacotes essenciais:

* `domain`: Contém as regras e lógica de negócio sem dependência de frameworks
* `use_cases`: Dependem apenas do pacote `domain` para executar as regras de negócio. Não podem depender do
  pacote `infrastructure`
* `infrastructure`: Contém detalhes técnicos, configurações, camadas de base de dados, controllers, ...e não podem
  conter regras de negócio. Podem possuir dependências com frameworks.

## 1 - Suba as dependências da aplicação com o docker-compose para os testes locais

```
docker-compose up -d
```



## 2 - Rode a aplicação localmente

```
./gradlew bootRun
```

## 2 - Rode a aplicação via docker

OBS:

1 -  Comente ou remova o comentário das linhas 37 e 38 conforme o seu sistema operacional

2 - Execute o comando docker compose abaixo (a imagem docker será criada através do parametro --build)

```
docker-compose -f docker-compose-app.yaml up -d --build  
```

3 - Para acessar os logs via kibana vá a URL http://localhost:5601/app/management/kibana/indexPatterns
e crie um index pattern com o texto fluentd-*. Salve e depois vá na seção "Discover", os logs da aplicação 
estarão listados ali

4 - Para parar a aplicação execute o comando
```
docker compose -f docker-compose-app.yaml down -v  
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

### Gere e execute uma imagem docker (mude o artefato {.jar})

```
./gradlew assemble
docker build --build-arg JAR_FILE=infrastructure/build/libs/infrastructure-1.0.0-SNAPSHOT.jar --tag=vv/cleanarch-spring .
docker run -p 8080:8080 --network="host" -ti -v /vv_logs/:/vv_logs/ --name cleanarch-relational vv/cleanarch-spring
```

### Geração de swagger do gRPC
https://github.com/grpc-swagger/grpc-swagger#build-and-run

```
wget https://github.com/grpc-swagger/grpc-swagger/releases/latest/download/grpc-swagger.jar 
java -jar grpc-swagger.jar --server.port=8080
```

Abra o link http://localhost:8080/ui/r.html
No campo `Grpc-swagger Server` digite:
```
http://localhost:8080
```

No campo `Endpoint Register` digite:
```
localhost:9091
```
Clique em `Register` e boa

## Install

```
./gradlew assemble
```

## Testes

```
./gradlew check
```