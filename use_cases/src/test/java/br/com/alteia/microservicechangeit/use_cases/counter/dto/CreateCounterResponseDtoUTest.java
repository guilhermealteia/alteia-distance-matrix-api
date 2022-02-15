package br.com.alteia.microservicechangeit.use_cases.counter.dto;

import br.com.alteia.microservicechangeit.entities.Counter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCounterResponseDtoUTest {

    @Nested
    class ConstructorShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Counter counter = new Counter(UUID.randomUUID().toString(), "Contador 2", 1000);

            // When
            CreateCounterResponseDto createCounterResponseDto = new CreateCounterResponseDto(counter);

            // Then
            assertEquals(counter.getId(), createCounterResponseDto.getId());
            assertEquals(counter.getName(), createCounterResponseDto.getName());
            assertEquals(counter.getValue(), createCounterResponseDto.getValue());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        CreateCounterResponseDto createCounterResponseDto1 = new CreateCounterResponseDto();
        createCounterResponseDto1.setId(id);
        createCounterResponseDto1.setName("Contador");
        createCounterResponseDto1.setValue(1000);

        CreateCounterResponseDto createCounterResponseDto2 = new CreateCounterResponseDto(null);
        createCounterResponseDto2.setId(createCounterResponseDto1.getId());
        createCounterResponseDto2.setName(createCounterResponseDto1.getName());
        createCounterResponseDto2.setValue(createCounterResponseDto1.getValue());

        assertDoesNotThrow(() -> new CreateCounterResponseDto(null));

        EqualsVerifier.simple().forClass(CreateCounterResponseDto.class).verify();

        int hashCode1 = createCounterResponseDto1.hashCode();
        int hashCode2 = createCounterResponseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

}