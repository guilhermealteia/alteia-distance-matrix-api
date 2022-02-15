package br.com.alteia.microservicechangeit.counter;

import br.com.alteia.microservicechangeit.entities.Counter;
import br.com.alteia.microservicechangeit.use_cases.counter.CreateCountersData;
import br.com.alteia.microservicechangeit.use_cases.counter.CreateUniqueCounterData;
import br.com.alteia.microservicechangeit.use_cases.counter.DecreaseCounter;
import br.com.alteia.microservicechangeit.use_cases.counter.GetUniqueCounterData;
import br.com.alteia.microservicechangeit.use_cases.counter.UpdateCounterData;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.CreateCounterRequestDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.CreateCounterResponseDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.GetCounterResponseDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.UpdateCounterRequestDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.UpdateCounterResponseDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CounterControllerUTest {

    @InjectMocks
    private CounterController counterController;

    @Mock
    private CreateUniqueCounterData createUniqueCounterData;

    @Mock
    private CreateCountersData createCountersData;

    @Mock
    private GetUniqueCounterData getUniqueCounterData;

    @Mock
    private UpdateCounterData updateCounterData;

    @Mock
    private DecreaseCounter decreaseCounter;

    @Nested
    class GetUniqueCounterDataShould {

        @Test
        void return_counter_data() {
            // Given
            String id = UUID.randomUUID().toString();
            GetCounterResponseDto getCounterResponseDto = new GetCounterResponseDto(id, "Contador 1", 1000);
            when(getUniqueCounterData.execute(id)).thenReturn(getCounterResponseDto);

            // When
            GetCounterResponseDto result = counterController.getCounterByIdV1(id);

            // Then
            assertEquals(result, getCounterResponseDto);
        }
    }

    @Nested
    class UpdateCounterShould {
        @Test
        void return_updated_counter_data() {
            // Given
            String id = UUID.randomUUID().toString();
            UpdateCounterRequestDto updateCounterRequestDto = new UpdateCounterRequestDto();
            updateCounterRequestDto.setId(id);
            updateCounterRequestDto.setName("Guilherme Alteia");
            updateCounterRequestDto.setValue(1000);

            // When
            UpdateCounterResponseDto updateCounterResponseDto = new UpdateCounterResponseDto();
            updateCounterResponseDto.setId(id);
            updateCounterResponseDto.setName("Guilherme Alteia");
            updateCounterRequestDto.setValue(1000);

            when(updateCounterData.execute(id, updateCounterRequestDto)).thenReturn(updateCounterResponseDto);

            UpdateCounterResponseDto result = counterController.updateCounterDataV1(id, updateCounterRequestDto);

            // Then
            assertTrue(new ReflectionEquals(result).matches(updateCounterResponseDto));
        }
    }

    @Nested
    class DecreaseCounterShould {
        @Test
        void decrease_counter_data() {
            // Given
            String id = UUID.randomUUID().toString();

            // When // Then
            assertDoesNotThrow(() -> counterController.decreaseCounterV1(id));
        }
    }

    @Nested
    class CreateCountersShould {
        @Test
        void create_counters_data() {
            // Given
            CreateCounterRequestDto createCounterRequestDto = new CreateCounterRequestDto();
            createCounterRequestDto.setName("Guilherme Alteia");
            createCounterRequestDto.setValue(1000);

            Counter counterInput = createCounterRequestDto.toCounter();

            // When // Then
            assertDoesNotThrow(() -> counterController.createCountersV1(singletonList(new CreateCounterRequestDto(counterInput))));
        }
    }

    @Nested
    class CreateUniqueCounterShould {
        @Test
        void create_unique_counter_data() {
            // Given
            CreateCounterRequestDto createCounterRequestDto = new CreateCounterRequestDto();
            createCounterRequestDto.setName("Guilherme Alteia");
            createCounterRequestDto.setValue(1000);

            Counter counterInput = createCounterRequestDto.toCounter();

            // When
            when(createUniqueCounterData.execute(createCounterRequestDto)).thenReturn(new CreateCounterResponseDto(counterInput));

            String json = new Gson().toJson(new CreateCounterRequestDto(counterInput));
            CreateCounterResponseDto result = counterController.createUniqueCounterV1(json);

            // Then
            assertTrue(new ReflectionEquals(result).matches(new CreateCounterResponseDto(counterInput)));
        }
    }
}