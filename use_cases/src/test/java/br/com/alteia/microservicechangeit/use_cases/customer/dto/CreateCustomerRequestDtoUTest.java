package br.com.alteia.microservicechangeit.use_cases.customer.dto;

import br.com.alteia.microservicechangeit.entities.customer.Customer;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCustomerRequestDtoUTest {

    @Nested
    class ToCustomerShould {

        @Test
        void create_new_entity_object_with_dto_value() {
            // Given
            String name = "Guilherme Alteia";
            LocalDate birthday = LocalDate.parse("1987-01-01");
            CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto(name, birthday.toString(), "42689636859");

            // When
            Customer customer = createCustomerRequestDto.toCustomer();

            // Then
            assertEquals(customer.getName(), name);
            assertEquals(birthday, customer.getBirthday());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        LocalDate date = LocalDate.now();

        CreateCustomerRequestDto customer1 = new CreateCustomerRequestDto();
        customer1.setName("Contador");
        customer1.setBirthday(date.toString());
        customer1.setInvoiceRecipientCpf("42689636859");

        CreateCustomerRequestDto customer2 = new CreateCustomerRequestDto(new Customer(null, "Contador", date, "42689636859"));
        CreateCustomerRequestDto customer3 = new CreateCustomerRequestDto("Contador", date.toString(), "42689636859");

        int hashCode1 = customer1.hashCode();
        int hashCode2 = customer2.hashCode();
        int hashCode3 = customer3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode2, hashCode3);

        assertDoesNotThrow(() -> new CreateCustomerRequestDto(null));
        EqualsVerifier.simple().forClass(CreateCustomerRequestDto.class).verify();
    }

}