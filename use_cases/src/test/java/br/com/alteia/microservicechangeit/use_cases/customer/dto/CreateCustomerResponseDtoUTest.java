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

class CreateCustomerResponseDtoUTest {

    @Nested
    class ToCustomerShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Customer customer = new Customer(UUID.randomUUID().toString(), "Guilherme", LocalDate.now(), CPF);

            // When
            CreateCustomerResponseDto createCustomerResponseDto = new CreateCustomerResponseDto(customer);

            // Then
            assertEquals(customer.getId(), createCustomerResponseDto.getId());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        CreateCustomerResponseDto customer1 = new CreateCustomerResponseDto();
        customer1.setId(id);

        CreateCustomerResponseDto customer2 = new CreateCustomerResponseDto(new Customer(id, "Contador", LocalDate.now(), CPF));

        int hashCode1 = customer1.hashCode();
        int hashCode2 = customer2.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertDoesNotThrow(() -> new CreateCustomerResponseDto(null));
        EqualsVerifier.simple().forClass(CreateCustomerResponseDto.class).verify();
    }

}