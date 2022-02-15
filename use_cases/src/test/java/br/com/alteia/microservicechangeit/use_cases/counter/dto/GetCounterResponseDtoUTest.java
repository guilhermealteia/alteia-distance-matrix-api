package br.com.alteia.microservicechangeit.use_cases.counter.dto;

import br.com.alteia.microservicechangeit.entities.Counter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetCounterResponseDtoUTest {

    @Nested
    class ConstructorShould {

        @Test
        void return_new_dto_object_with_entity_value() {
            // Given
            Counter counter = new Counter(UUID.randomUUID().toString(), "Contador 2", 1000);

            // When
            GetCounterResponseDto getCounterResponseDto = new GetCounterResponseDto(counter);

            // Then
            assertEquals(counter.getId(), getCounterResponseDto.getId());
            assertEquals(counter.getName(), getCounterResponseDto.getName());
            assertEquals(counter.getValue(), getCounterResponseDto.getValue());
        }
    }

    @Test
    void validate_class_have_required_methods() {
        String id = UUID.randomUUID().toString();

        GetCounterResponseDto getCounterResponseDto1 = new GetCounterResponseDto();
        getCounterResponseDto1.setId(id);
        getCounterResponseDto1.setName("Contador");
        getCounterResponseDto1.setValue(1000);

        GetCounterResponseDto getCounterResponseDto2 = new GetCounterResponseDto(id, "Contador", 1000);

        assertDoesNotThrow(() -> new GetCounterResponseDto(null));

        EqualsVerifier.simple().forClass(GetCounterResponseDto.class).verify();

        int hashCode1 = getCounterResponseDto1.hashCode();
        int hashCode2 = getCounterResponseDto2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

}