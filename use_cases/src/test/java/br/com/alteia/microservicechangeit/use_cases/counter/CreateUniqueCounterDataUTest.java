package br.com.alteia.microservicechangeit.use_cases.counter;

import br.com.alteia.microservicechangeit.entities.Counter;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.CreateCounterRequestDto;
import br.com.alteia.microservicechangeit.use_cases.counter.dto.CreateCounterResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static br.com.alteia.microservicechangeit.test_utils.CounterGenerator.mockCounter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUniqueCounterDataUTest {

    private CreateUniqueCounterData createUniqueCounterData;

    @Mock
    private CounterRepository counterRepository;

    @BeforeEach
    void setUp() {
        createUniqueCounterData = new CreateUniqueCounterData(counterRepository);
    }

    @Test
    void create_counter_data() {
        // Dados os elementos abaixo
        String id = UUID.randomUUID().toString();
        Counter counter = mockCounter(id);

        when(counterRepository.save(counter)).thenReturn(counter);

        // Quando
        CreateCounterResponseDto result = createUniqueCounterData.execute(new CreateCounterRequestDto(counter));

        // Então
        assertEquals(result.getName(), counter.getName());
        assertEquals(result.getValue(), counter.getValue());
    }
}