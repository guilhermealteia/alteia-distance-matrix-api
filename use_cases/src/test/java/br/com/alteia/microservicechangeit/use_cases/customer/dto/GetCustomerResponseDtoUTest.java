package br.com.alteia.microservicechangeit.use_cases.customer.dto;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CustomerGenerator.CPF;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetCustomerResponseDtoUTest {

    @Nested
    class ToCustomerShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Customer customer = new Customer(UUID.randomUUID().toString(), "Guilherme Alteia", LocalDate.now(), CPF);

            // When
            GetCustomerResponseDto getCustomerResponseDto = new GetCustomerResponseDto(customer);

            // Then
            assertEquals(customer.getId(), getCustomerResponseDto.getId());
            assertEquals(customer.getName(), getCustomerResponseDto.getName());
            assertEquals(customer.getBirthday().toString(), getCustomerResponseDto.getBirthday());
            assertEquals(customer.getInvoiceRecipientCpf(), getCustomerResponseDto.getCpf());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        LocalDate date = LocalDate.now();
        String id = UUID.randomUUID().toString();

        GetCustomerResponseDto getCustomerResponseDto1 = new GetCustomerResponseDto();
        getCustomerResponseDto1.setId(id);
        getCustomerResponseDto1.setName("Guilherme Alteia");
        getCustomerResponseDto1.setBirthday(date.toString());
        getCustomerResponseDto1.setCpf(CPF);

        GetCustomerResponseDto getCustomerResponseDto2 = new GetCustomerResponseDto(new Customer(id, "Guilherme Alteia", date, CPF));

        int hashCode1 = getCustomerResponseDto1.hashCode();
        int hashCode2 = getCustomerResponseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertDoesNotThrow(() -> new GetCustomerResponseDto(null));
        EqualsVerifier.simple().forClass(GetCustomerResponseDto.class).verify();
    }

}