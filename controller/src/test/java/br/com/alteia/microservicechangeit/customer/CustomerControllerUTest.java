package br.com.alteia.microservicechangeit.customer;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import br.com.alteia.microservicechangeit.use_cases.customer.CreateCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.GetAllCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.GetUniqueCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.UpdateCustomerData;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerRequestDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.CreateCustomerResponseDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.GetCustomerResponseDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.UpdateCustomerRequestDto;
import br.com.alteia.microservicechangeit.use_cases.customer.dto.UpdateCustomerResponseDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.NAME;
import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.mockCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerUTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CreateCustomerData createCustomerData;

    @Mock
    private GetAllCustomerData getAllCustomerData;

    @Mock
    private GetUniqueCustomerData getUniqueCustomerData;

    @Mock
    private UpdateCustomerData updateCustomerData;

    @Nested
    class GetUniqueCustomerDataShould {

        @Test
        void return_customer_data() {
            // Given
            String id = UUID.randomUUID().toString();
            GetCustomerResponseDto responseDto = new GetCustomerResponseDto(new Customer(id, NAME, LocalDate.now(), CPF));
            when(getUniqueCustomerData.execute(id)).thenReturn(responseDto);

            // When
            GetCustomerResponseDto result = customerController.getCustomerByIdV1(id);

            // Then
            assertEquals(result, responseDto);
        }
    }

    @Nested
    class GetAllCustomerDataShould {

        @Test
        void return_customer_data() {
            // Given
            List<GetCustomerResponseDto> customerData = List.of(
                    new GetCustomerResponseDto(
                            new Customer(null, NAME, LocalDate.now(), CPF)
                    )
            );
            when(getAllCustomerData.execute()).thenReturn(customerData);

            // When
            List<GetCustomerResponseDto> result = customerController.getAllCustomersV1();

            // Then
            assertEquals(result, customerData);
        }
    }

    @Nested
    class CreateCustomerDataShould {

        @Test
        void create_customer_using_dto_value() {
            // Given
            String name = "Guilherme Alteia";
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto(name, "2020-10-12", CPF);

            // When
            when(createCustomerData.execute(createCustomerRequestDto)).thenReturn(
                    new CreateCustomerResponseDto(
                            mockCustomer(
                                    createCustomerRequestDto.toCustomer().getName(),
                                    createCustomerRequestDto.toCustomer().getBirthday().toString(),
                                    CPF
                            )
                    )
            );
            customerController.createCustomerDataV1(createCustomerRequestDto);

            // Then
            ArgumentCaptor<CreateCustomerRequestDto> argumentCaptor = ArgumentCaptor.forClass(CreateCustomerRequestDto.class);
            verify(createCustomerData).execute(argumentCaptor.capture());
            CreateCustomerRequestDto customerToCreate = argumentCaptor.getValue();
            assertEquals(customerToCreate.getName(), name);
        }

        @Test
        void return_created_customer_data() {
            // Given
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto("Guilherme Alteia", "2020-01-01", CPF);

            Customer customer = new Customer(
                    null,
                    createCustomerRequestDto.getName(),
                    LocalDate.parse(createCustomerRequestDto.getBirthday()),
                    createCustomerRequestDto.getInvoiceRecipientCpf()
            );

            when(createCustomerData.execute(createCustomerRequestDto)).thenReturn(new CreateCustomerResponseDto(customer));

            // When
            CreateCustomerResponseDto result = customerController.createCustomerDataV1(createCustomerRequestDto);

            // Then
            assertTrue(new ReflectionEquals(result).matches(new CreateCustomerResponseDto(customer)));
        }
    }

    @Nested
    class UpdateCustomerDataShould {
        @Test
        void return_updated_customer_data() {
            // Given
            String id = UUID.randomUUID().toString();
            UpdateCustomerRequestDto updateCustomerRequestDto = new UpdateCustomerRequestDto();
            updateCustomerRequestDto.setId(id);
            updateCustomerRequestDto.setName("Guilherme Alteia");
            updateCustomerRequestDto.setBirthday("2020-01-01");
            updateCustomerRequestDto.setCpf(CPF);

            Customer customer = new Customer(
                    id,
                    updateCustomerRequestDto.getName(),
                    LocalDate.parse(updateCustomerRequestDto.getBirthday()),
                    CPF
            );

            when(updateCustomerData.execute(id, updateCustomerRequestDto)).thenReturn(new UpdateCustomerResponseDto(customer));

            // When
            UpdateCustomerResponseDto result = customerController.updateCustomerDataV1(id, updateCustomerRequestDto);

            // Then
            assertEquals(result, new UpdateCustomerResponseDto(customer));
        }
    }

}