package br.com.alteia.microservicechangeit.use_cases.counter.dto;

import br.com.alteia.microservicechangeit.entities.Counter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCounterResponseDtoUTest {

    @Nested
    class ConstructorShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Counter counter = new Counter(UUID.randomUUID().toString(), "Contador 2", 1000);

            // When
            UpdateCounterResponseDto updateCounterResponseDto = new UpdateCounterResponseDto(counter);

            // Then
            assertEquals(counter.getId(), updateCounterResponseDto.getId());
            assertEquals(counter.getName(), updateCounterResponseDto.getName());
            assertEquals(counter.getValue(), updateCounterResponseDto.getValue());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        UpdateCounterResponseDto updateCounterResponseDto1 = new UpdateCounterResponseDto();
        updateCounterResponseDto1.setId(id);
        updateCounterResponseDto1.setName("Contador");
        updateCounterResponseDto1.setValue(1000);

        UpdateCounterResponseDto updateCounterResponseDto2 = new UpdateCounterResponseDto();
        updateCounterResponseDto2.setId(id);
        updateCounterResponseDto2.setName("Contador");
        updateCounterResponseDto2.setValue(1000);

        assertDoesNotThrow(() -> new UpdateCounterResponseDto(null));

        EqualsVerifier.simple().forClass(UpdateCounterResponseDto.class).verify();

        int hashCode1 = updateCounterResponseDto1.hashCode();
        int hashCode2 = updateCounterResponseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

}