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

class UpdateCustomerResponseDtoUTest {

    @Nested
    class ToCustomerShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Customer customer = new Customer(UUID.randomUUID().toString(), "Guilherme Alteia", LocalDate.now(), CPF);

            // When
            UpdateCustomerResponseDto responseDto = new UpdateCustomerResponseDto(customer);

            // Then
            assertEquals(customer.getId(), responseDto.getId());
            assertEquals(customer.getName(), responseDto.getName());
            assertEquals(customer.getBirthday().toString(), responseDto.getBirthday());
            assertEquals(customer.getInvoiceRecipientCpf(), responseDto.getCpf());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        LocalDate date = LocalDate.now();
        String id = UUID.randomUUID().toString();

        UpdateCustomerResponseDto responseDto1 = new UpdateCustomerResponseDto();
        responseDto1.setId(id);
        responseDto1.setName("Guilherme Alteia");
        responseDto1.setBirthday(date.toString());
        responseDto1.setCpf(CPF);

        UpdateCustomerResponseDto responseDto2 = new UpdateCustomerResponseDto(new Customer(id, "Guilherme Alteia", date, CPF));

        int hashCode1 = responseDto1.hashCode();
        int hashCode2 = responseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertDoesNotThrow(() -> new UpdateCustomerResponseDto(null));
        EqualsVerifier.simple().forClass(UpdateCustomerResponseDto.class).verify();
    }

}