package br.com.alteia.microservicechangeit.use_cases.counter.dto;

import br.com.alteia.microservicechangeit.entities.Counter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCounterRequestDtoUTest {

    @Nested
    class ToCounterShould {

        @Test
        void return_new_entity_object_with_dto_value() {
            // Given
            String name = "Guilherme Alteia";
            UpdateCounterRequestDto updateCounterRequestDto = new UpdateCounterRequestDto();
            updateCounterRequestDto.setId(UUID.randomUUID().toString());
            updateCounterRequestDto.setName(name);
            updateCounterRequestDto.setValue(1000);

            // When
            Counter counter = updateCounterRequestDto.toCounter();

            // Then
            assertEquals(counter.getName(), name);
            assertEquals(1000, counter.getValue());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        UpdateCounterRequestDto updateCounterRequestDto1 = new UpdateCounterRequestDto();
        updateCounterRequestDto1.setId(id);
        updateCounterRequestDto1.setName("Contador");
        updateCounterRequestDto1.setValue(1000);

        UpdateCounterRequestDto updateCounterRequestDto2 = new UpdateCounterRequestDto();
        updateCounterRequestDto2.setId(id);
        updateCounterRequestDto2.setName("Contador");
        updateCounterRequestDto2.setValue(1000);

        assertDoesNotThrow(() -> new UpdateCounterRequestDto(null));

        EqualsVerifier.simple().forClass(UpdateCounterRequestDto.class).verify();

        int hashCode1 = updateCounterRequestDto1.hashCode();
        int hashCode2 = updateCounterRequestDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

}