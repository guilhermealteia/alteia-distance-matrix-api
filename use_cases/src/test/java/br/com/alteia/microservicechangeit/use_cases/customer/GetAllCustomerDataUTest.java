package br.com.alteia.microservicechangeit.use_cases.customer;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.GetCustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.mockCustomer;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCustomerDataUTest {

    private GetAllCustomerData getAllCustomerData;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        getAllCustomerData = new GetAllCustomerData(customerRepository);
    }

    @Test
    void return_all_customers_data() {
        // Dados os elementos abaixo
        Customer customer1 = mockCustomer("Olavo Bilac", "1865-12-16", CPF);

        Customer customer2 = mockCustomer("Parnasiano Teles", "1875-12-16", CPF);

        List<Customer> customerData = asList(
                customer1, customer2
        );
        when(customerRepository.findAll()).thenReturn(customerData);

        // Quando
        List<GetCustomerResponseDto> result = getAllCustomerData.execute();

        // Então
        assertTrue(new ReflectionEquals(result).matches(customerData.stream().map(GetCustomerResponseDto::new).toList()));
    }
}