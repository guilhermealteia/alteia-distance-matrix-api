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

class UpdateCustomerRequestDtoUTest {

    @Nested
    class ToCustomerShould {

        @Test
        void return_new_entity_object_with_dto_value() {
            // Given
            String name = "Guilherme Alteia";
            LocalDate birthday = LocalDate.parse("1987-01-01");
            UpdateCustomerRequestDto updateCustomerRequestDto = new UpdateCustomerRequestDto();
            updateCustomerRequestDto.setId(UUID.randomUUID().toString());
            updateCustomerRequestDto.setName(name);
            updateCustomerRequestDto.setBirthday(birthday.toString());
            updateCustomerRequestDto.setCpf(CPF);

            // When
            Customer customer = updateCustomerRequestDto.toCustomer();

            // Then
            assertEquals(customer.getName(), name);
            assertEquals(birthday, customer.getBirthday());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        LocalDate date = LocalDate.now();
        String id = UUID.randomUUID().toString();

        UpdateCustomerRequestDto updateCustomerRequestDto1 = new UpdateCustomerRequestDto();
        updateCustomerRequestDto1.setId(id);
        updateCustomerRequestDto1.setName("Customer name");
        updateCustomerRequestDto1.setBirthday(date.toString());
        updateCustomerRequestDto1.setCpf(CPF);

        UpdateCustomerRequestDto updateCustomerRequestDto2 = new UpdateCustomerRequestDto(new Customer(id, "Customer name", date, CPF));

        int hashCode1 = updateCustomerRequestDto1.hashCode();
        int hashCode2 = updateCustomerRequestDto2.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertDoesNotThrow(() -> new UpdateCustomerRequestDto(null));
        EqualsVerifier.simple().forClass(UpdateCustomerRequestDto.class).verify();
    }

}