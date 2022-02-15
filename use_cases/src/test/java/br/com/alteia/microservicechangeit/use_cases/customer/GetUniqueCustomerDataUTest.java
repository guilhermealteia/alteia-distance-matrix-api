package br.com.alteia.microservicechangeit.use_cases.customer;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.GetCustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.CustomerUseCaseErrorMessages.CUSTOMER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUniqueCustomerDataUTest {

    private GetUniqueCustomerData getUniqueCustomerData;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        getUniqueCustomerData = new GetUniqueCustomerData(customerRepository);
    }

    @Test
    void return_unique_customer_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Customer customer = new Customer(id, "Mazzaropi Neves", LocalDate.parse("1995-12-02"), CPF);

        when(customerRepository.findCustomerById(id)).thenReturn(customer);

        // Quando
        GetCustomerResponseDto result = getUniqueCustomerData.execute(id);

        // Então
        assertEquals(new GetCustomerResponseDto(customer), result);
    }

    @Test
    void should_throw_exception_get_customer_data_that_not_exists() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        String code = null;
        String errorMessage = null;

        // Quando
        try {
            getUniqueCustomerData.execute(id);
        } catch (NotFoundException ex) {
            code = ex.getCode();
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(CUSTOMER_NOT_FOUND.getCode(), code);
        assertEquals(String.format(CUSTOMER_NOT_FOUND.getMessage(), id), errorMessage);
    }
}