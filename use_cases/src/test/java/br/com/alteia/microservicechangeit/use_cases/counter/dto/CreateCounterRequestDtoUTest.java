package br.com.alteia.microservicechangeit.use_cases.counter.dto;

import br.com.alteia.microservicechangeit.entities.Counter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCounterRequestDtoUTest {

    @Nested
    class ToCounterShould {

        @Test
        void return_new_entity_object_with_dto_value() {
            // Given
            String name = "Guilherme Alteia";
            CreateCounterRequestDto createCounterRequestDto = new CreateCounterRequestDto();
            createCounterRequestDto.setName(name);
            createCounterRequestDto.setValue(1000);

            // When
            Counter counter = createCounterRequestDto.toCounter();

            // Then
            assertEquals(counter.getName(), name);
            assertEquals(1000, counter.getValue());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        CreateCounterRequestDto createCounterRequestDto1 = new CreateCounterRequestDto();
        createCounterRequestDto1.setName("Contador");
        createCounterRequestDto1.setValue(1000);

        CreateCounterRequestDto createCounterRequestDto2 = new CreateCounterRequestDto();
        createCounterRequestDto2.setName("Contador");
        createCounterRequestDto2.setValue(1000);

        assertDoesNotThrow(() -> new CreateCounterRequestDto(null));

        EqualsVerifier.simple().forClass(CreateCounterRequestDto.class).verify();

        int hashCode1 = createCounterRequestDto1.hashCode();
        int hashCode2 = createCounterRequestDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals("CreateCounterRequestDto{name='Contador', value=1000}", createCounterRequestDto1.toString());
    }

}