package br.com.alteia.microservicechangeit.use_cases.customer;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerRequestDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.mockCustomer;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerDataUTest {

    private CreateCustomerData createCustomerData;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        createCustomerData = new CreateCustomerData(customerRepository);
    }

    @Test
    void return_save_and_return_saved_customer_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Customer customer = mockCustomer(id);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Quando
        CreateCustomerResponseDto result = createCustomerData.execute(new CreateCustomerRequestDto(customer));

        // Então
        assertTrue(new ReflectionEquals(result).matches(new CreateCustomerResponseDto(customer)));
    }

}