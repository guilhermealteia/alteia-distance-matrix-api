package br.com.alteia.microservicechangeit.use_cases.customer;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.MismatchException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.UpdateCustomerRequestDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.UpdateCustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.mockCustomer;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.CustomerUseCaseErrorMessages.CUSTOMERID_MISMATCH;
import static br.com.alteia.microservicechangeit.use_cases.common.enums.CustomerUseCaseErrorMessages.CUSTOMER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerDataUTest {

    private UpdateCustomerData updateCustomerData;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        updateCustomerData = new UpdateCustomerData(customerRepository);
    }

    @Test
    void update_customer_data_without_changes() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Customer customer = mockCustomer(id);

        when(customerRepository.findCustomerById(id)).thenReturn(customer);

        // Quando
        UpdateCustomerResponseDto result = updateCustomerData.execute(id, new UpdateCustomerRequestDto(customer));

        // Então
        assertTrue(new ReflectionEquals(result).matches(new UpdateCustomerResponseDto(customer)));

        verify(customerRepository).findCustomerById(id);

        //Importante essa validação para garantir que
        // o método que verifica a necessidade do uptade realmente funcionou
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void update_and_return_updated_customer_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();

        Customer customer = mockCustomer(id);
        Customer updatedCustomer = mockCustomer(id, "Nome Alterado", "1994-06-01", CPF);

        when(customerRepository.findCustomerById(id)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Quando
        UpdateCustomerResponseDto result = updateCustomerData.execute(id, new UpdateCustomerRequestDto(updatedCustomer));

        // Então
        assertTrue(new ReflectionEquals(result).matches(new UpdateCustomerResponseDto(updatedCustomer)));
    }

    @Test
    void should_throw_exception_update_customer_data_with_different_ids() {
        // Dados os elementos abaixo
        String id1 = UUID.randomUUID().toString();
        Customer customer = mockCustomer(id1);

        String code = null;
        String errorMessage = null;

        String id2 = UUID.randomUUID().toString();
        // Quando
        try {
            updateCustomerData.execute(id2, new UpdateCustomerRequestDto(customer));
        } catch (MismatchException ex) {
            code = ex.getCode();
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(CUSTOMERID_MISMATCH.getCode(), code);
        assertEquals(String.format(CUSTOMERID_MISMATCH.getMessage(), id2, id1), errorMessage);
    }

    @Test
    void should_throw_exception_update_customer_data_that_not_exists() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Customer customer = mockCustomer(id);

        String code = null;
        String errorMessage = null;

        // Quando
        try {
            updateCustomerData.execute(id, new UpdateCustomerRequestDto(customer));
        } catch (NotFoundException ex) {
            code = ex.getCode();
            errorMessage = ex.getMessage();
        }

        // Então
        assertEquals(CUSTOMER_NOT_FOUND.getCode(), code);
        assertEquals(String.format(CUSTOMER_NOT_FOUND.getMessage(), id), errorMessage);
    }

}