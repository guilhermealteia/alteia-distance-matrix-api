package br.com.alteia.microservicechangeit.adapters.repositories;

import br.com.alteia.microservicechangeit.CleanArchitectureChangeItApplication;
import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = {CleanArchitectureChangeItApplication.class}, properties = {"grpc.server.port=9093"})
class CustomerJPARepositoryITest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer = new DockerComposeContainer<>(new File("docker-compose-test.yaml"))
            .withLocalCompose(true);

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        customer = new Customer(null, "Guilherme Alteia", LocalDate.parse("2000-12-31"), "03250932027");
    }

    @Nested
    class CreateCustomerMethodShould {

        @Test
        void save_should_persist_customer() {
            // Dados os elementos abaixo
            Customer secondCustomer = new Customer(null, "Guilhermino Altea", LocalDate.parse("2000-12-31"), "94247587072");
            Customer firstPersist = customerRepository.save(customer);

            // Quando
            Customer secondPersist = customerRepository.save(secondCustomer);

            // Então
            assertThat(secondPersist.getId()).isNotEqualTo(firstPersist.getId());
            assertThat(secondPersist.getName()).isEqualTo("Guilhermino Altea");
            assertThat(secondPersist.getBirthday()).isEqualTo("2000-12-31");
        }
    }

    @Nested
    class findAllCustomersMethodShould {
        @Test
        void findAll_should_return_all_customers() {
            // Dado
            Customer secondCustomer = new Customer(null, "Guilhermino Altea", LocalDate.parse("1998-06-01"), "12007519062");

            customerRepository.save(customer);
            customerRepository.save(secondCustomer);

            // Quando
            List<Customer> found = customerRepository.findAll();

            // Então
            assertThat(found.size()).isEqualTo(2);
            assertThat(found.get(0)).isEqualTo(customer);
            assertThat(found.get(1)).isEqualTo(secondCustomer);
        }
    }

    @Nested
    class findByIdMethodShould {
        @Test
        void findById_should_return_specific_customer() {
            // Dado
            customerRepository.save(customer);

            // Quando
            Customer found = customerRepository.findCustomerById(customer.getId());

            // Então
            assertThat(found.equals(customer)).isTrue();
        }
    }
}
